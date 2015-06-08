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
//app.use(bodyParser);
/*app.use( bodyParser.json() );       // to support JSON-encoded bodies
app.use(bodyParser.urlencoded({     // to support URL-encoded bodies
  extended: true
})); */
function validProperty(key)
{
	if(key === 'targetlink')
		return false;
	if(key === 'targetmethod')
		return false;
	return true;
}

app.get('/',function(req,res){
	res.end("Hello World")
})
app.post('/', function(req, res) {
	var targetlink=req.headers.targetlink;
	var targetmethod=req.headers.targetmethod;
	console.log(targetlink+" "+targetmethod)
	if(targetmethod.toUpperCase() === 'POST')
	{

		var realheader={}

		for(key in req.headers)
		{

			if(typeof req.headers[key] !== 'undefined' && validProperty(key))
			{
				realheader[key]=req.headers[key];
			}
		}
		realheader["host"]=url.parse(targetlink).hostname;
		//var mystrings=fs.readFileSync("./result1.html");
		//res.writeHead(200, "OK",{});
		//res.end(mystrings+mystrings);	
		var start=Date.now();
		var httpreq = httpsync.request({
		  url: targetlink,
		  method: targetmethod,
		 headers: realheader});
		httpreq.write(req.rawBody);
		var httpresponse=httpreq.end();	
		var end=Date.now();
		console.log(end-start);	
		res.writeHead(200, "OK",httpresponse.headers);
		fs.writeFileSync("./result.html",httpresponse.data.toString());
		res.end(httpresponse.data.toString());
	}
	res.end();

    // ...
});

var server = app.listen(1988, function () {

  var host = server.address().address;
  var port = server.address().port;

  console.log('Example app listening at http://%s:%s', host, port);

});
