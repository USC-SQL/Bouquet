var http = require('http');
var zlib = require('zlib');
var fs = require('fs');
var request = require('request');
var querystring = require('querystring');
var utils = require('utils');

http.createServer(function (req, res) {
 

    if(req.method=='POST') {
            var body='';
            req.on('data', function (data) {
                body +=data;
            });
            req.on('end',function(){
                
                var POST =  querystring.parse(body);
                console.log(POST);
		res.end()
            });
    }
    else if(req.method=='GET') {
        var url_parts = url.parse(req.url,true);
        console.log(url_parts.query);
    }


}
).listen(1988)

