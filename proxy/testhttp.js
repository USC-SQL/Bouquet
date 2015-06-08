var express = require('express');
var http = require('http');
var zlib = require('zlib');
var fs = require('fs');
var request = require('request');
var querystring = require('querystring');
var utils = require('utils');

var bodyParser = require('body-parser')
var body="FromStation=105&ToStation=1&RequestDate=05%2F01%2F2015&RequestTime=01%3A00&RequestAMPM=AM&sortBy=1&schedules.x=71&schedules.y=58"
var realheader={ 
  cookie: 'PHPSESSID=o392qkqbel8evld6f5c03mdvt9hkj6o3',
  'content-length': body.length,
  'content-type': 'application/x-www-form-urlencoded',
  //host: 'lirr42.mta.info/index.php',
  connection: 'Keep-Alive',
  'user-agent': 'Mozilla/5.0 (Linux; U; Android 1.1; en-us;dream) AppleWebKit/525.10+ (KHTML, like Gecko) Version/3.0.4 Mobile Safari/523.12.2' 
}

var httpsync = require('httpsync');
var req = httpsync.request({
  url: "http://lirr42.mta.info/index.php",
  method: "POST",
 headers: { 
  'content-length': '0',
  host: 'lirr42.mta.info',
  connection: 'Keep-Alive',
  'user-agent': 'Mozilla/5.0 (Linux; U; Android 1.1; en-us;dream) AppleWebKit/525.10+ (KHTML, like Gecko) Version/3.0.4 Mobile Safari/523.12.2' }

});
var response=req.end();
fs.writeFileSync("./result.html",response.data.toString());
var responsecoockie=response.headers['set-cookie'];
//console.log(response);
console.log(responsecoockie);
var cookie = request.cookie(responsecoockie);
console.log(cookie)
var start = Date.now();
var req1 = httpsync.request({
  url: "http://lirr42.mta.info/index.php",
  method: "POST",
 headers: { 
  'Cookie': responsecoockie,
  'content-length': body.length,
  connection: 'Keep-Alive',
  host: 'lirr42.mta.info',
  'user-agent': 'Mozilla/5.0 (Linux; U; Android 1.1; en-us;dream) AppleWebKit/525.10+ (KHTML, like Gecko) Version/3.0.4 Mobile Safari/523.12.2' }

});
req1.write(body)
var response1=req1.end();
fs.writeFileSync("./result1.html",response1.data.toString());
//console.log(response1.headers);

var req2 = httpsync.request({
  url: "http://lirr42.mta.info/schedules.php",
  method: "POST",
 headers: { 
  'Cookie': responsecoockie,
  'content-length': body.length,
  connection: 'Keep-Alive',
  host: 'lirr42.mta.info',
  'user-agent': 'Mozilla/5.0 (Linux; U; Android 1.1; en-us;dream) AppleWebKit/525.10+ (KHTML, like Gecko) Version/3.0.4 Mobile Safari/523.12.2' }

});
req2.write(body)
var response2=req2.end();
fs.writeFileSync("./result2.html",response2.data.toString());
//console.log(response2.headers);
var end = Date.now();
console.log(end-start);
/*

var query ={ FromStation: '105',
  ToStation: '1',
  RequestDate: '04/30/2015',
  RequestTime: '09:00',
  RequestAMPM: 'PM',
  sortBy: '1',
  'schedules.x': '75',
  'schedules.y': '6' }

var post_options = {
	host: "lirr42.mta.info",
	path: "/index.php",
	method: 'POST',
       headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
            'Content-Length': Buffer.byteLength(body),
	    connection: 'Keep-Alive',
	    'user-agent': 'Mozilla/5.0 (Linux; U; Android 1.1; en-us;dream) AppleWebKit/525.10+ (KHTML, like Gecko) Version/3.0.4 Mobile Safari/523.12.2' 
        }
	//headers: realheader
}
var post_req=http.request(post_options, function(response) {
			console.log(response.headers)
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
						//console.log(decoded.toString());

					});
				} else if (encoding == 'deflate') {
					zlib.inflate(buffer, function(err, decoded) {
						//console.log(decoded.toString());
					})

				} else {
					//console.log(buffer.toString());
				}
			});
		  });
post_req.write(body);
post_req.end();*/



