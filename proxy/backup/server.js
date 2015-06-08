var http = require('http');
var zlib = require('zlib');
var fs = require('fs');
var request = require('request');
var querystring = require('querystring');
var utils = require('utils');
function Recorder(decodedBody,res){
	this.limit=decodedBody.numberofrequest;
	this.cnt=0;
	this.responsestr={};
	this.response=res;
	this.PutResonseString=function (link,str){
		if(this.cnt==this.limit)
		{
			return;
		}
		this.responsestr[link]=str;
		this.cnt++;
		if(this.cnt==this.limit)
		{
			var s=JSON.stringify(this.responsestr);
		    	var buf = new Buffer(s, 'utf-8');
			var myresponse=this.response;
		    	zlib.gzip(buf, function (_, result) {  
				console.log("I am here");

				myresponse.write(result);
				myresponse.end();                 
		   	 });

		}
	}
	
}
function CallbackGenerator(recorder,link)
{
    var mylink=link;
    return function(response) {
	     var chunks = [];
	  //another chunk of data has been recieved, so append it to `str`
	  var encoding=response.headers['content-encoding']
	  response.on('data', function (chunk) {
	          chunks.push(chunk);
	  });

	  //the whole response has been recieved, so we just print it out here
	response.on('end', function() {
	      var buffer = Buffer.concat(chunks);
	     if (encoding == 'gzip') {
		zlib.gunzip(buffer, function(err, decoded) {
		   	console.log(mylink+"  "+decoded.toString().length);
	    		recorder.PutResonseString(mylink,decoded.toString());

		});
	      } else if (encoding == 'deflate') {
		     zlib.inflate(buffer, function(err, decoded) {
			  console.log(mylink+" "+decoded.toString().length);
	    		  recorder.PutResonseString(mylink,decoded.toString());
			})

	      } else {
			console.log(mylink+" "+encoding+" "+buffer.toString().length);
	    		recorder.PutResonseString(mylink,buffer.toString());
	      }
	    });
	}
}



http.createServer(function (req, res) {
 
  var myheaders=req.headers;
  if(req.method="GET")
  {
	
  }
 if (req.method == 'POST') {
    console.log("[200] " + req.method + " to " + req.url);
    var fullBody = '';
    res.writeHead(200, "OK", {'Content-Type': 'text/html','content-encoding':'gzip'});
    req.on('data', function(chunk) {
      // append the current chunk of data to the fullBody variable
      fullBody += chunk.toString();
    });
    
    req.on('end', function() {
    
      // request ended -> do something with the data
      var decodedBody = querystring.parse(fullBody);
      var recorder=new Recorder(decodedBody,res);

      var requestnum=decodedBody.numberofrequest


      for(var i=0;i<requestnum;i++)
	{
		
		var options = {
		  url: decodedBody["requ"+i],
		  method: 'GET',
		  headers: {'user-agent':myheaders['user-agent'],
			     connection:myheaders["connection"],
			      'accept-encoding': myheaders['accept-encoding'],
                             'content-type': myheaders['content-type']}
		};
		var req=request.get(options);
		req.on('response',CallbackGenerator(recorder,decodedBody["requ"+i]))
		req.on('error',function(){    res.writeHead(404, "Method not supported", {'Content-Type': 'text/html'});res.end();})
	}
    });
    
  } else {
    console.log("[405] " + req.method + " to " + req.url);
    res.writeHead(405, "Method not supported", {'Content-Type': 'text/html'});
    res.end('<html><head><title>405 - Method not supported</title></head><body><h1>Method not supported.</h1></body></html>');
  }



}
).listen(1988)
