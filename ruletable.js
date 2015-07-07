//bobweather
var regexbob0=new RegExp(/http:\/\/api.openweathermap.org\/data\/2\.5\/weather\?q=([A-Za-z0-9\+]+)&mode=xml&units=metric&APPID=1e38049ae254a0d6c627d624dc13c9fd/);
var regexbob1=new RegExp(/http:\/\/api.openweathermap.org\/data\/2.5\/forecast\/daily\?q=([A-Za-z0-9\+]+)&mode=xml&units=metric&cnt=5&APPID=1e38049ae254a0d6c627d624dc13c9fd/);
var regexbob2=new RegExp(/http:\/\/api.worldweatheronline.com\/free\/v1\/tz.ashx\?q=([A-Za-z0-9\+]+)&format=xml&key=umre32dzkzma6bkwm9k88zgf/);
var bobweatherule = function (link){
	var match=regexbob0.exec(link)
	var pack={main:link,bundled:[]};

		if(match!=null)
	{
		var link1="http://api.openweathermap.org/data/2.5/forecast/daily?q="+match[1]+"&mode=xml&units=metric&cnt=5&APPID=1e38049ae254a0d6c627d624dc13c9fd"
		var link2="http://api.worldweatheronline.com/free/v1/tz.ashx?q="+match[1]+"&format=xml&key=umre32dzkzma6bkwm9k88zgf"
		pack.bundled.push(link1);
		pack.bundled.push(link2);
		return pack;
	}
	return pack;

}
//var p=bobweatherule("http://api.openweathermap.org/data/2.5/weather?q=Los+Angeles&mode=xml&units=metric&APPID=1e38049ae254a0d6c627d624dc13c9fd")

//LIRR
var regexLIRR0=new RegExp(/http:\/\/lirr42.mta.info\/index.php/);
var regexLIRR1=new RegExp(/http:\/\/lirr42.mta.info\/index.php\?(.+)/);
var regexLIRR2=new RegExp(/http:\/\/lirr42.mta.info\/schedules.php\?(.+)/);
var LIRRrule = function (link){
	var match=regexLIRR1.exec(link)
	var pack={main:link,bundled:[]};

		if(match!=null)
	{
		var link1="http:\/\/lirr42.mta.info\/schedules.php\?"+match[1];
		pack.bundled.push(link1);
		return pack;
	}
	return pack;

}
//ALJA
var regexALJA0=new RegExp(/http:\/\/mis-aljazeera.gos2m.nl\/MisLogM2Auth\/aljazeera\/at\/([A-Za-z0-9\+]+)\/([A-Za-z0-9\+]+)\/startup\?store=(android)&version=1.1.0/);
var regexALJA1=new RegExp(/http:\/\/mis-aljazeera.gos2m.nl\/MisLogM2Auth\/aljazeera\/at\/([A-Za-z0-9\+]+)\/([A-Za-z0-9\+]+)\/startup\?store=(android)&version=1.1.0/);
//Tapjoy
var regexTapjoy0=new RegExp(/https:\/\/ws.tapjoyads.com\/videos\?.+/);
var regexTapjoy1=new RegExp(/https:\/\/ws.tapjoyads.com\/connect\?.+/);
var regexTapjoy2=new RegExp(/https:\/\/s3.amazonaws.com\/tapjoy\/videos\/assets\/watermark.png/);
var tapjoyrule = function (link){
	var match=regexTapjoy2.exec(link)
	var pack={main:link,bundled:[]};

		if(match!=null)
	{
		var link1="https://s3.amazonaws.com/tapjoy/videos/assets/watermark.png"
		pack.bundled.push(link1);
		return pack;
	}
	return pack;

}
//pch
var regexpch0=new RegExp(/https:\/\/ws.tapjoyads.com\/videos\?(.+)/);
var regexpch1=new RegExp(/https:\/\/connect.tapjoy.com\/connect\?(.+)/);
var regexpch2=new RegExp(/http:\/\/appclick.co\/PublicServices\/MobileTrackingApiRestV1\.svc\/AppWasRunV2\/Put(.+)/);
var regrule=[];
regrule.push({grep:regexbob0,rule:bobweatherule});
//regrule.push({grep:regexTapjoy2,rule:tapjoyrule});
regrule.push({grep:regexLIRR1,rule:LIRRrule});

module.exports.findpack = function(link){
	var r={main:link,bundled:[]};
	regrule.forEach(function (obj){
		if(obj.grep.exec(link)!=null)
		{
			var pack=obj.rule(link);
			r=pack;
		}

	})
	r.bundled.push(link);
	return r;
}
