var httpsync = require('httpsync');
var zlib = require('zlib');

var header={targetlink:"http://mis-aljazeera.gos2m.nl/MisLogM2Auth/aljazeera/at/431a42c5d93c5f2367ac2f212da6478b/1435277663891/startup?store=android&version=1.1.0"}
var httpreq = httpsync.request({
  url:" ubuntu@ec2-52-11-252-130.us-west-2.compute.amazonaws.com:1989",
  method: "GET",
  headers:header});
 // httpreq.write("FromStation=107&ToStation=109&RequestDate=06%2F17%2F2015&RequestTime=07%3A45&RequestAMPM=PM&sortBy=1&schedules.x=16&schedules.y=34");
//console.log(realheader);
var httpresponse=httpreq.end();
zlib.unzip(httpresponse.data, function(err, buffer) {
  if (!err) {
    console.log(buffer.toString());
  }
});
//console.log(httpresponse.data.toString());
