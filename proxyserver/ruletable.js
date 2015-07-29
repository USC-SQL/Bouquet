//generated bobweather
var Session_0_0_rex0_0=new RegExp(/http:\/\/api\.openweathermap\.org\/data\/2\.5\/weather\?lat=a&lon=b&mode=xml&units=metric&APPID=1e38049ae254a0d6c627d624dc13c9fd/);
var Session_0_0_rex0_1=new RegExp(/http:\/\/api\.openweathermap\.org\/data\/2\.5\/weather\?q=([A-Za-z0-9+]+)&mode=xml&units=metric&APPID=1e38049ae254a0d6c627d624dc13c9fd/);
var Session_0_0_rex0_2=new RegExp(/http:\/\/api\.openweathermap\.org\/data\/2\.5\/weather\?lat=([A-Za-z0-9+]+)&mode=xml&units=metric&APPID=1e38049ae254a0d6c627d624dc13c9fd/);
var Session_0_0_rex0_3=new RegExp(/http:\/\/api\.openweathermap\.org\/data\/2\.5\/weather\?lat=([A-Za-z0-9+]+)&lon=a&mode=xml&units=metric&APPID=1e38049ae254a0d6c627d624dc13c9fd/);
var Session_0_0_rule = function (link){
    var pack={main:link,bundled:[]};
    Macther_0_0_rex0_0=Session_0_0_rex0_0.exec(link)
    if(Macther_0_0_rex0_0!=null)
    {
			//The incomming link has pattern: http://api.openweathermap.org/data/2.5/weather\?q=([A-Za-z0-9+]+)&mode=xml&units=metric&APPID=1e38049ae254a0d6c627d624dc13c9fd
			/*
			The Next 1 link has patterns:
			http://api.openweathermap.org/data/2.5/forecast/daily\?lat=a&lon=b&mode=xml&units=metric&cnt=5&APPID=1e38049ae254a0d6c627d624dc13c9fd
			http://api.openweathermap.org/data/2.5/forecast/daily\?q=([A-Za-z0-9+]+)&mode=xml&units=metric&cnt=5&APPID=1e38049ae254a0d6c627d624dc13c9fd
			http://api.openweathermap.org/data/2.5/forecast/daily\?lat=([A-Za-z0-9+]+)&mode=xml&units=metric&cnt=5&APPID=1e38049ae254a0d6c627d624dc13c9fd
			http://api.openweathermap.org/data/2.5/forecast/daily\?lat=([A-Za-z0-9+]+)&lon=a&mode=xml&units=metric&cnt=5&APPID=1e38049ae254a0d6c627d624dc13c9fd
			Please add these patterns to pack.bundled
			*/
			/*
			The Next 2 link has patterns:
			http://api.worldweatheronline.com/free/v1/tz.ashx\?q=([A-Za-z0-9+]+)&format=xml&key=umre32dzkzma6bkwm9k88zgf
			http://api.worldweatheronline.com/free/v1/tz.ashx\?q=a,b&format=xml&key=umre32dzkzma6bkwm9k88zgf
			http://api.worldweatheronline.com/free/v1/tz.ashx\?q=([A-Za-z0-9+]+),a&format=xml&key=umre32dzkzma6bkwm9k88zgf
			Please add these patterns to pack.bundled
			*/
			console.log("match to bundle");
      var link1="http://api.openweathermap.org/data/2.5/forecast/daily?q="+Macther_0_rex0_0[1]+"&mode=xml&units=metric&cnt=5&APPID=1e38049ae254a0d6c627d624dc13c9fd"
      var link2="http://api.worldweatheronline.com/free/v1/tz.ashx?q="+Macther_0_rex0_0[1]+"&format=xml&key=umre32dzkzma6bkwm9k88zgf"
      pack.bundled.push(link1);
      pack.bundled.push(link2);
			return pack;
    }
    Macther_0_0_rex0_1=Session_0_0_rex0_1.exec(link)
    if(Macther_0_0_rex0_1!=null)
    {
			//The incomming link has pattern: http://api.openweathermap.org/data/2.5/weather\?lat=([A-Za-z0-9+]+)&lon=a&mode=xml&units=metric&APPID=1e38049ae254a0d6c627d624dc13c9fd
			/*
			The Next 1 link has patterns:
			http://api.openweathermap.org/data/2.5/forecast/daily\?lat=a&lon=b&mode=xml&units=metric&cnt=5&APPID=1e38049ae254a0d6c627d624dc13c9fd
			http://api.openweathermap.org/data/2.5/forecast/daily\?q=([A-Za-z0-9+]+)&mode=xml&units=metric&cnt=5&APPID=1e38049ae254a0d6c627d624dc13c9fd
			http://api.openweathermap.org/data/2.5/forecast/daily\?lat=([A-Za-z0-9+]+)&mode=xml&units=metric&cnt=5&APPID=1e38049ae254a0d6c627d624dc13c9fd
			http://api.openweathermap.org/data/2.5/forecast/daily\?lat=([A-Za-z0-9+]+)&lon=a&mode=xml&units=metric&cnt=5&APPID=1e38049ae254a0d6c627d624dc13c9fd
			Please add these patterns to pack.bundled
			*/
			/*
			The Next 2 link has patterns:
			http://api.worldweatheronline.com/free/v1/tz.ashx\?q=([A-Za-z0-9+]+)&format=xml&key=umre32dzkzma6bkwm9k88zgf
			http://api.worldweatheronline.com/free/v1/tz.ashx\?q=a,b&format=xml&key=umre32dzkzma6bkwm9k88zgf
			http://api.worldweatheronline.com/free/v1/tz.ashx\?q=([A-Za-z0-9+]+),a&format=xml&key=umre32dzkzma6bkwm9k88zgf
			Please add these patterns to pack.bundled
			*/
      var link1="http://api.openweathermap.org/data/2.5/forecast/daily?lat="+Macther_0_rex0_1[1]+"&lon=a&mode=xml&units=metric&cnt=5&APPID=1e38049ae254a0d6c627d624dc13c9fd"
      var link2="http://api.worldweatheronline.com/free/v1/tz.ashx?q="+Macther_0_rex0_1[1]+"&format=xml&key=umre32dzkzma6bkwm9k88zgf"
      pack.bundled.push(link1);
      pack.bundled.push(link2);
			return pack;
    }
    Macther_0_0_rex0_2=Session_0_0_rex0_2.exec(link)
    if(Macther_0_0_rex0_2!=null)
    {
			//The incomming link has pattern: http://api.openweathermap.org/data/2.5/weather\?lat=a&lon=b&mode=xml&units=metric&APPID=1e38049ae254a0d6c627d624dc13c9fd
			/*
			The Next 1 link has patterns:
			http://api.openweathermap.org/data/2.5/forecast/daily\?lat=a&lon=b&mode=xml&units=metric&cnt=5&APPID=1e38049ae254a0d6c627d624dc13c9fd
			http://api.openweathermap.org/data/2.5/forecast/daily\?q=([A-Za-z0-9+]+)&mode=xml&units=metric&cnt=5&APPID=1e38049ae254a0d6c627d624dc13c9fd
			http://api.openweathermap.org/data/2.5/forecast/daily\?lat=([A-Za-z0-9+]+)&mode=xml&units=metric&cnt=5&APPID=1e38049ae254a0d6c627d624dc13c9fd
			http://api.openweathermap.org/data/2.5/forecast/daily\?lat=([A-Za-z0-9+]+)&lon=a&mode=xml&units=metric&cnt=5&APPID=1e38049ae254a0d6c627d624dc13c9fd
			Please add these patterns to pack.bundled
			*/
			/*
			The Next 2 link has patterns:
			http://api.worldweatheronline.com/free/v1/tz.ashx\?q=([A-Za-z0-9+]+)&format=xml&key=umre32dzkzma6bkwm9k88zgf
			http://api.worldweatheronline.com/free/v1/tz.ashx\?q=a,b&format=xml&key=umre32dzkzma6bkwm9k88zgf
			http://api.worldweatheronline.com/free/v1/tz.ashx\?q=([A-Za-z0-9+]+),a&format=xml&key=umre32dzkzma6bkwm9k88zgf
			Please add these patterns to pack.bundled
			*/
      var link1="http://api.openweathermap.org/data/2.5/forecast/daily?lat=a&lon&mode=xml&units=metric&cnt=5&APPID=1e38049ae254a0d6c627d624dc13c9fd"
      var link2="http://api.worldweatheronline.com/free/v1/tz.ashx?q=a,b&format=xml&key=umre32dzkzma6bkwm9k88zgf"
      pack.bundled.push(link1);
      pack.bundled.push(link2);
			return pack;
    }
    Macther_0_0_rex0_3=Session_0_0_rex0_3.exec(link)
    if(Macther_0_0_rex0_3!=null)
    {
			//The incomming link has pattern: http://api.openweathermap.org/data/2.5/weather\?lat=([A-Za-z0-9+]+)&mode=xml&units=metric&APPID=1e38049ae254a0d6c627d624dc13c9fd
			/*
			The Next 1 link has patterns:
			http://api.openweathermap.org/data/2.5/forecast/daily\?lat=a&lon=b&mode=xml&units=metric&cnt=5&APPID=1e38049ae254a0d6c627d624dc13c9fd
			http://api.openweathermap.org/data/2.5/forecast/daily\?q=([A-Za-z0-9+]+)&mode=xml&units=metric&cnt=5&APPID=1e38049ae254a0d6c627d624dc13c9fd
			http://api.openweathermap.org/data/2.5/forecast/daily\?lat=([A-Za-z0-9+]+)&mode=xml&units=metric&cnt=5&APPID=1e38049ae254a0d6c627d624dc13c9fd
			http://api.openweathermap.org/data/2.5/forecast/daily\?lat=([A-Za-z0-9+]+)&lon=a&mode=xml&units=metric&cnt=5&APPID=1e38049ae254a0d6c627d624dc13c9fd
			Please add these patterns to pack.bundled
			*/
			/*
			The Next 2 link has patterns:
			http://api.worldweatheronline.com/free/v1/tz.ashx\?q=([A-Za-z0-9+]+)&format=xml&key=umre32dzkzma6bkwm9k88zgf
			http://api.worldweatheronline.com/free/v1/tz.ashx\?q=a,b&format=xml&key=umre32dzkzma6bkwm9k88zgf
			http://api.worldweatheronline.com/free/v1/tz.ashx\?q=([A-Za-z0-9+]+),a&format=xml&key=umre32dzkzma6bkwm9k88zgf
			Please add these patterns to pack.bundled
			*/
      var link1="http://api.openweathermap.org/data/2.5/forecast/daily?lat="+Macther_0_rex0_1[1]+"&mode=xml&units=metric&cnt=5&APPID=1e38049ae254a0d6c627d624dc13c9fd"
      var link2="http://api.worldweatheronline.com/free/v1/tz.ashx?q="+Macther_0_rex0_1[1]+"&format=xml&key=umre32dzkzma6bkwm9k88zgf"
      pack.bundled.push(link1);
      pack.bundled.push(link2);
			return pack;
    }
	return pack;
}

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
var Session_3_0_rex0_0=new RegExp(/https:\/\/s3\.amazonaws\.com\/tapjoy\/videos\/assets\/watermark\.png/);
var Session_3_0_rule = function (link){
    var pack={main:link,bundled:[]};
    Macther_3_0_rex0_0=Session_3_0_rex0_0.exec(link)
    console.log("It is here 111");

    if(Macther_3_0_rex0_0!=null)
    {
      console.log("It is here");
			//The incomming link has pattern: https:\/\/s3\.amazonaws\.com\/tapjoy\/videos\/assets\/watermark\.png
			/*
			The Next 1 link has patterns:
			https://s3.amazonaws.com/tapjoy/videos/assets/watermark.png
			Please add these patterns to pack.bundled
			*/
			/*
			Extra Dexription of reuqests
			Request 0 has the same address as 1
			*/

			return pack;
    }
	return pack;
}
//pch
var regexpch0=new RegExp(/https:\/\/ws.tapjoyads.com\/videos\?(.+)/);
var regexpch1=new RegExp(/https:\/\/connect.tapjoy.com\/connect\?(.+)/);
var regexpch2=new RegExp(/http:\/\/appclick.co\/PublicServices\/MobileTrackingApiRestV1\.svc\/AppWasRunV2\/Put(.+)/);
var regrule=[];
//regrule.push({grep:Session_0_0_rex0_0,rule:Session_0_0_rule});
regrule.push({grep:regexbob0,rule:bobweatherule});
regrule.push({grep:Session_3_0_rex0_0,rule:Session_3_0_rule});
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
