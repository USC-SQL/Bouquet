var express = require('express');
var fs=require('fs')
var zlib=require('zlib')
var bodyParser = require('body-parser')
var proxyrules= require('./ruletable.js');
var request = require('request');
var httpsync = require('httpsync');
var start;
var end;
var getraw= function(req, res, next){
   var data = "";
   req.on('data', function(chunk){ data += chunk;console.log("nana")})
   req.on('end', function(){
      req.rawBody = data;
      console.log("lala")
      next();
   })
}
var app = express();
app.use(getraw);
var lookupserverURL="http://localhost:1988"
function querypackGET(options,res, cnt,pack,wholebuffer)
{
  console.log(cnt);
  if(cnt>=pack.bundled.length)
  {
    return;
  }
  options.headers.targetlink=pack.bundled[cnt];
  var targetlink=pack.bundled[cnt];
  var req=request.get(options);
  //var wholebuffer=new Buffer("");
  req.on('response',function(response) {
       var chunks = [];
    var encoding=response.headers['content-encoding']
    response.on('data', function (chunk) {
      chunks.push(chunk);
    });
    var newheader={};

    //the whole response has been recieved, so we just print it out here
  response.on('end', function() {
        var linkandseperater = new Buffer(targetlink+"###")
        var contentbuffer = Buffer.concat(chunks);
        var seperater =  new Buffer("@@@");
	//var wholebuffer = new Buffer("");
	zlib.unzip(contentbuffer, function(err,buffer)
	{
		        var responsebuffer = Buffer.concat([linkandseperater,buffer,seperater]);
        	if(cnt==0)
        	{
        	  console.log("headers");
        	  res.writeHead(response.statusCode,response.headers)
	          //wholebuffer=Buffer.concat([responsebuffer]);

        	}
		wholebuffer=Buffer.concat([wholebuffer,responsebuffer]);
        	//res.write(responsebuffer)
        	if(cnt>=pack.bundled.length-1)
        	{
		  console.log(cnt+" "+pack.bundled.length)
		  end=Date.now();
		  console.log("server time: "+(end-start));
		  zlib.gzip(wholebuffer,function(err,result){
			res.end(result);	
		  });
        	  //res.end();
        	}
        	else{
        	  querypackGET(options,res,cnt+1,pack,wholebuffer)
        	}
 		
	})
      //console.log(encoding)
      });
   });
  req.on('error',function(){    res.writeHead(404, "Method not supported", {'Content-Type': 'text/html'});res.end();})
}
function querypackPOST(options,res,pack)
{


  for(var i=0;i<pack.bundled.length;i++)
  {

    var responsebuffer;

    var content=pack.bundled[i].split("\?");
    var link=content[0];
    var parameters=content[1];
    var linkandseperater = new Buffer(link+"?"+parameters+"###")
    var seperater =  new Buffer("@@@");
    options.headers.targetlink=link;

    var httpreq = httpsync.request(options);
      httpreq.write(parameters);
    //console.log(realheader);
    var httpresponse=httpreq.end();

    var responsebuffer = Buffer.concat([linkandseperater,httpresponse.data,seperater]);
    if(i==0)
    {
      console.log("headers");
      res.writeHead(httpresponse.statusCode,httpresponse.headers)

    }
    res.write(responsebuffer)
  }
  res.end();

}
app.get('/',function(req,res){
	console.log(req.headers)
  var targetlink=req.headers.targetlink;
  var pack=proxyrules.findpack(targetlink);
  console.log(pack);
  var realheader={}
  for(key in req.headers)
  {

    if(typeof req.headers[key] !== 'undefined')
    {
      realheader[key]=req.headers[key];

      //console.log(key+": "+req.headers[key])
    }
  }
  var options = {
    url: lookupserverURL,
    headers: realheader,
    method: "GET"
  };
  start=Date.now();
  querypackGET(options,res, 0,pack,new Buffer(""))

})
app.post('/', function(req, res) {
  var targetlink=req.headers.targetlink;
		var realheader={}

		for(key in req.headers)
		{

			if(typeof req.headers[key] !== 'undefined')
			{
				realheader[key]=req.headers[key];
			}
		}

		var query=targetlink+"?"+req.rawBody.toString();
    var pack=proxyrules.findpack(query);
    console.log(pack);
    var realheader={}
    for(key in req.headers)
    {
      if(typeof req.headers[key] !== 'undefined')
      {
        realheader[key]=req.headers[key];
        //console.log(key+": "+req.headers[key])
      }
    }
    var options = {
      url: lookupserverURL,
      headers: realheader,
      method: "POST"
    };
    querypackPOST(options,res,pack);
    // ...
});

var server = app.listen(1989, function () {

  var host = server.address().address;
  var port = server.address().port;

  console.log('Example app listening at http://%s:%s', host, port);

});
