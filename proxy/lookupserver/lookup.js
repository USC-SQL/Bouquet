var express = require('express');
var app = express();
var fs=require('fs')
var bodyParser = require('body-parser')
app.use( bodyParser.json() );       // to support JSON-encoded bodies
app.use(bodyParser.urlencoded({     // to support URL-encoded bodies
  extended: true
})); 
var lookup_table={

}
app.get('/',function(req,res){
	console.log(req.headers)
	var seperater = new Buffer("@@@")
	fs.readFile('./watermark.png', function (err,data) {
	  if (err) {
	    return console.log(err);
	  }
          console.log(data.length);
	  var buff=Buffer.concat([seperater,data,seperater])
	  console.log(buff);
	  res.end(buff)
	  //res.end()
	  //console.log(data);
	});

	//res.end("Hello World")
})
app.post('/', function(req, res) {
	console.log(req.body)
	console.log(req.headers)
	res.send('Hello World!');	
    // ...
});

var server = app.listen(1988, function () {

  var host = server.address().address;
  var port = server.address().port;

  console.log('Example app listening at http://%s:%s', host, port);

});
