var http = require('http');
var request = require('request');
var url = require('url');
var zlib = require('zlib');
//link1: http://api.openweathermap.org/data/2.5/weather?q=Los+Angeles&mode=xml&units=metric&APPID=1e38049ae254a0d6c627d624dc13c9fd
//link2: http://api.openweathermap.org/data/2.5/forecast/daily?q=Los+Angeles&mode=xml&units=metric&cnt=5&APPID=1e38049ae254a0d6c627d624dc13c9fd
//link3: http://api.worldweatheronline.com/free/v1/tz.ashx?q=Los+Angeles&format=xml&key=umre32dzkzma6bkwm9k88zgf
var link1 = "http://api.openweathermap.org/data/2.5/weather?q=Los+Angeles&mode=xml&units=metric&APPID=1e38049ae254a0d6c627d624dc13c9fd"
var link2 = "http://api.openweathermap.org/data/2.5/forecast/daily?q=Los+Angeles&mode=xml&units=metric&cnt=5&APPID=1e38049ae254a0d6c627d624dc13c9fd"
var link3 = "http://api.worldweatheronline.com/free/v1/tz.ashx?q=Los+Angeles&format=xml&key=umre32dzkzma6bkwm9k88zgf"
var predicttable = {}
//predicttable[link1]=[link1,link2,link3];
//console.log(predicttable[link1])
function ResponseBundle(list, cleintresponse) {
    this.linklist = list;
    this.cnt = 0;
    this.limit = list.length;
    this.response = cleintresponse;
    this.responsecache = {};
    this.AddResponse = function (targetlink, body) {
        this.responsecache[targetlink] = targetlink + "@@@" + body;
        this.cnt++;
        if (this.cnt == this.limit) {
            //console.log(this.responsecache);
            var r = "";
            var pin = 0;
            for (var key in this.responsecache) {
                if (pin === 0) {
                    r += this.responsecache[key];
                }
                else {
                    r += "!!!" + this.responsecache[key];
                }
                pin++;
            }
            console.log(r);
            this.response.end(r);
        }


    }

}

function CallbackGenerator(resbundle, tagetlink) {
    return function (response) {
        var chunks = [];
        //another chunk of data has been recieved, so append it to `str`
        var encoding = response.headers['content-encoding']
        response.on('data', function (chunk) {
            chunks.push(chunk);
        });

        //the whole response has been recieved, so we just print it out here
        response.on('end', function () {
            var buffer = Buffer.concat(chunks);
            var r = ""

            if (encoding == 'gzip') {

                zlib.gunzip(buffer, function (err, decoded) {
                    //console.log(decoded.toString().length);
                    //r=tagetlink+"@@@"+decoded.toString()
                    //var buf=new Buffer("###link:"+tagetlink+"content:"+decoded.toString()+"***");
                    resbundle.AddResponse(tagetlink, decoded.toString())
                });


            } else if (encoding == 'deflate') {
                zlib.inflate(buffer, function (err, decoded) {
                    //console.log(decoded.toString().length);
                    //console.log(decoded.toString());
                    //r=tagetlink+"@@@"+decoded.toString()
                    resbundle.AddResponse(tagetlink, decoded.toString())

                })

            } else {
                //console.log(buffer.toString().length);
                // console.log(buffer.toString());
                //r=tagetlink+"@@@"+buffer.toString()
                resbundle.AddResponse(tagetlink, buffer.toString())
            }
        });
    }
}

function reuqestList(list, myheaders, clientresponse) {
    var cnt = 0;
    var limit = list.length;
    var responsetable = {};
    var resbundle = new ResponseBundle(list, clientresponse);
    for (var i = 0; i < list.length; i++) {
        var mylink = list[i];
        var options = {
            url: mylink,
            method: 'GET',
            headers: myheaders
        };
        var realreq = request.get(options);
        realreq.on('response', CallbackGenerator(resbundle, mylink))


    }

}
http.createServer(function (req, res) {

        var myheaders = req.headers;
        if (req.method = "GET") {
            var url_parts = url.parse(req.url, true);
            var targetlink = url_parts.query.targetlink;
            console.log(targetlink)
            if (targetlink in predicttable) {
                var list = predicttable[targetlink]
                // console.log(list);
                reuqestList(list, {
                    'user-agent': myheaders['user-agent'],
                    connection: myheaders["connection"],
                    'accept-encoding': myheaders['accept-encoding'],
                    'content-type': myheaders['content-type']
                }, res)

            }
            else {
                reuqestList([targetlink], {
                    'user-agent': myheaders['user-agent'],
                    connection: myheaders["connection"],
                    'accept-encoding': myheaders['accept-encoding'],
                    'content-type': myheaders['content-type']
                }, res)
            }

        }


    }
).listen(1988)
/*function Recorder(decodedBody,res){
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
 function CallbackGenerator(toclient,tagetlink)
 {
 var clientresponse=toclient;
 return function(response) {
 var chunks = [];
 //another chunk of data has been recieved, so append it to `str`
 var encoding=response.headers['content-encoding']
 console.log('encoding '+encoding);
 response.on('data', function (chunk) {
 chunks.push(chunk);
 });

 //the whole response has been recieved, so we just print it out here
 response.on('end', function() {
 var buffer = Buffer.concat(chunks);
 var r=""

 if (encoding == 'gzip') {

 zlib.gunzip(buffer, function(err, decoded) {
 console.log(decoded.toString().length);
 r=tagetlink+"@@@"+decoded.toString()
 //var buf=new Buffer("###link:"+tagetlink+"content:"+decoded.toString()+"***");
 clientresponse.end(r);

 });


 } else if (encoding == 'deflate') {
 zlib.inflate(buffer, function(err, decoded) {
 console.log(decoded.toString().length);
 console.log(decoded.toString());
 r=tagetlink+"@@@"+decoded.toString()
 clientresponse.end(r);

 })

 } else {
 console.log(buffer.toString().length);
 console.log(buffer.toString());
 r=tagetlink+"@@@"+buffer.toString()
 clientresponse.end(r);

 }
 });
 }
 }


 http.createServer(function (req, res) {

 var myheaders=req.headers;
 if(req.method="GET")
 {
 var url_parts = url.parse(req.url, true);
 var targetlink = url_parts.query.targetlink;
 console.log(targetlink)
 var options = {
 url: targetlink,
 method: 'GET',
 headers: {'user-agent':myheaders['user-agent'],
 connection:myheaders["connection"],
 'accept-encoding': myheaders['accept-encoding'],
 'content-type': myheaders['content-type']}
 };
 var realreq=request.get(options);
 realreq.on('response',CallbackGenerator(res,targetlink))
 realreq.on('error',function(){    res.writeHead(404, "Method not supported", {'Content-Type': 'text/html'});res.end();})

 }



 }
 ).listen(1988)*/