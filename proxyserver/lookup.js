var express = require('express');
var http = require('http');
var zlib = require('zlib');
var fs = require('fs');
var request = require('request');
var querystring = require('querystring');
var utils = require('utils');
var bodyParser = require('body-parser')
var httpsync = require('httpsync');
var url = require('url');
var getraw= function(req, res, next){
   var data = "";
   req.on('data', function(chunk){ data += chunk})
   req.on('end', function(){
      req.rawBody = data;
      next();
   })
}
var app = express();
app.use(getraw);
//all regular expressions
///////////////////////////////////
var gregulars=require("./regulartable.js");

function validProperty(key)
{
	if(key === 'targetlink')
		return false;
	if(key === 'targetmethod')
		return false;
	return true;
}
var totolcnt=0;
app.get('/',function(req,res){
	var targetlink=req.headers.targetlink;
	//console.log(targetlink+" GET ")
	var realheader={}
	for(key in req.headers)
	{

		if(typeof req.headers[key] !== 'undefined' && validProperty(key))
		{
			realheader[key]=req.headers[key];
			//console.log(key+": "+req.headers[key])
		}
	}
	realheader["host"]=url.parse(targetlink).hostname;
	realheader["host"]=url.parse(targetlink).hostname;
	var start=Date.now();
	var stored=gregulars.lookup(targetlink);
	if(stored!=null)
	{
		console.log("match ")
		var f=fs.readFileSync(stored.file+"/data");
		var header=JSON.parse(fs.readFileSync(stored.file+"/header"));
		var sendheader={};
		if(header["set-cookie"])
		{
			sendheader["set-cookie"]=header["set-cookie"];

		}
    sendheader['content-encoding']='gzip';

		res.writeHead(200,sendheader)

    zlib.gzip(f,function(err,result){

      res.end(result);
    });
		//res.end(f);

	}
	else{
      		console.log("not match! "+targetlink)
      		var options = {
      		  url: targetlink,
      		  headers: realheader,
      		  method: "GET"
      		};
      		//console.log(realheader);
      		var req=request.get(options);
      		req.on('response',function(response) {
      		     var chunks = [];
      		  var encoding=response.headers['content-encoding']
      		  response.on('data', function (chunk) {
      			  chunks.push(chunk);
      		  });
      		  var newheader={};

      		  //the whole response has been recieved, so we just print it out here
      		response.on('end', function() {

      		      var buffer = Buffer.concat(chunks);
      		      res.writeHead(response.statusCode,response.headers)
      		      res.end(buffer)
      		     //console.log(encoding)
      		    });
      		})
      		req.on('error',function(){    res.writeHead(404, "Method not supported", {'Content-Type': 'text/html'});res.end();})
		}


})
app.post('/', function(req, res) {
	var targetlink=req.headers.targetlink;

	//console.log(targetlink)


		var realheader={}

		for(key in req.headers)
		{

			if(typeof req.headers[key] !== 'undefined' && validProperty(key))
			{
				realheader[key]=req.headers[key];
			}
		}
		var query=targetlink+"?"+req.rawBody.toString();
		var stored=gregulars.lookup(query);
		if(stored!=null)
		{
			console.log("match")
			var f=fs.readFileSync(stored.file+"/data");
			var header=JSON.parse(fs.readFileSync(stored.file+"/header"));
			var sendheader={};
			if(header["set-cookie"])
			{
				sendheader["set-cookie"]=header["set-cookie"];

			}
			res.writeHead(200,sendheader)
			res.end(f);

		}
		else{
			console.log("not match! "+query)
		realheader["host"]=url.parse(targetlink).hostname;
		var start=Date.now();
		var httpreq = httpsync.request({
		  url: targetlink,
		  method: "POST",
		 headers: realheader});
		httpreq.write(req.rawBody);
		//console.log(realheader);
		var httpresponse=httpreq.end();
		var end=Date.now();
		//console.log(end-start);
		//console.log(req.rawBody.toString('utf8'));
		res.writeHead(200, "OK",httpresponse.headers);
    /*fs.mkdirSync("./"+totolcnt);
    fs.writeFileSync("./"+totolcnt+"/parameter",req.rawBody.toString('utf8'));
    fs.writeFileSync("./"+totolcnt+"/link",targetlink);
    fs.writeFileSync("./"+totolcnt+"/header",JSON.stringify(httpresponse.headers));
    fs.writeFileSync("./"+totolcnt+"/data",httpresponse.data);*/
		totolcnt++;
		res.end(httpresponse.data.toString());
		}



});

var server = app.listen(1988, function () {

  var host = server.address().address;
  var port = server.address().port;

  console.log('Example app listening at http://%s:%s', host, port);

});
