var Session_0_0_rex0_0=new Rex(/http://api.openweathermap.org/data/2.5/weather\?q=([A-Za-z0-9+]+)&mode=xml&units=metric&APPID=1e38049ae254a0d6c627d624dc13c9fd/);
var Session_0_0_rex0_1=new Rex(/http://api.openweathermap.org/data/2.5/weather\?lat=([A-Za-z0-9+]+)&lon=a&mode=xml&units=metric&APPID=1e38049ae254a0d6c627d624dc13c9fd/);
var Session_0_0_rex0_2=new Rex(/http://api.openweathermap.org/data/2.5/weather\?lat=a&lon=b&mode=xml&units=metric&APPID=1e38049ae254a0d6c627d624dc13c9fd/);
var Session_0_0_rex0_3=new Rex(/http://api.openweathermap.org/data/2.5/weather\?lat=([A-Za-z0-9+]+)&mode=xml&units=metric&APPID=1e38049ae254a0d6c627d624dc13c9fd/);
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

var Session_0_1_rex0_0=new Rex(/http://api.openweathermap.org/data/2.5/forecast/daily\?lat=a&lon=b&mode=xml&units=metric&cnt=5&APPID=1e38049ae254a0d6c627d624dc13c9fd/);
var Session_0_1_rex0_1=new Rex(/http://api.openweathermap.org/data/2.5/forecast/daily\?q=([A-Za-z0-9+]+)&mode=xml&units=metric&cnt=5&APPID=1e38049ae254a0d6c627d624dc13c9fd/);
var Session_0_1_rex0_2=new Rex(/http://api.openweathermap.org/data/2.5/forecast/daily\?lat=([A-Za-z0-9+]+)&mode=xml&units=metric&cnt=5&APPID=1e38049ae254a0d6c627d624dc13c9fd/);
var Session_0_1_rex0_3=new Rex(/http://api.openweathermap.org/data/2.5/forecast/daily\?lat=([A-Za-z0-9+]+)&lon=a&mode=xml&units=metric&cnt=5&APPID=1e38049ae254a0d6c627d624dc13c9fd/);
var Session_0_1_rule = function (link){
    var pack={main:link,bundled:[]};
    Macther_0_1_rex0_0=Session_0_1_rex0_0.exec(link)
    if(Macther_0_1_rex0_0!=null)
    {
			//The incomming link has pattern: http://api.openweathermap.org/data/2.5/forecast/daily\?lat=a&lon=b&mode=xml&units=metric&cnt=5&APPID=1e38049ae254a0d6c627d624dc13c9fd
			/*
			The Next 1 link has patterns:
			http://api.worldweatheronline.com/free/v1/tz.ashx\?q=([A-Za-z0-9+]+)&format=xml&key=umre32dzkzma6bkwm9k88zgf
			http://api.worldweatheronline.com/free/v1/tz.ashx\?q=a,b&format=xml&key=umre32dzkzma6bkwm9k88zgf
			http://api.worldweatheronline.com/free/v1/tz.ashx\?q=([A-Za-z0-9+]+),a&format=xml&key=umre32dzkzma6bkwm9k88zgf
			Please add these patterns to pack.bundled
			*/
			return pack;
    }
    Macther_0_1_rex0_1=Session_0_1_rex0_1.exec(link)
    if(Macther_0_1_rex0_1!=null)
    {
			//The incomming link has pattern: http://api.openweathermap.org/data/2.5/forecast/daily\?q=([A-Za-z0-9+]+)&mode=xml&units=metric&cnt=5&APPID=1e38049ae254a0d6c627d624dc13c9fd
			/*
			The Next 1 link has patterns:
			http://api.worldweatheronline.com/free/v1/tz.ashx\?q=([A-Za-z0-9+]+)&format=xml&key=umre32dzkzma6bkwm9k88zgf
			http://api.worldweatheronline.com/free/v1/tz.ashx\?q=a,b&format=xml&key=umre32dzkzma6bkwm9k88zgf
			http://api.worldweatheronline.com/free/v1/tz.ashx\?q=([A-Za-z0-9+]+),a&format=xml&key=umre32dzkzma6bkwm9k88zgf
			Please add these patterns to pack.bundled
			*/
			return pack;
    }
    Macther_0_1_rex0_2=Session_0_1_rex0_2.exec(link)
    if(Macther_0_1_rex0_2!=null)
    {
			//The incomming link has pattern: http://api.openweathermap.org/data/2.5/forecast/daily\?lat=([A-Za-z0-9+]+)&mode=xml&units=metric&cnt=5&APPID=1e38049ae254a0d6c627d624dc13c9fd
			/*
			The Next 1 link has patterns:
			http://api.worldweatheronline.com/free/v1/tz.ashx\?q=([A-Za-z0-9+]+)&format=xml&key=umre32dzkzma6bkwm9k88zgf
			http://api.worldweatheronline.com/free/v1/tz.ashx\?q=a,b&format=xml&key=umre32dzkzma6bkwm9k88zgf
			http://api.worldweatheronline.com/free/v1/tz.ashx\?q=([A-Za-z0-9+]+),a&format=xml&key=umre32dzkzma6bkwm9k88zgf
			Please add these patterns to pack.bundled
			*/
			return pack;
    }
    Macther_0_1_rex0_3=Session_0_1_rex0_3.exec(link)
    if(Macther_0_1_rex0_3!=null)
    {
			//The incomming link has pattern: http://api.openweathermap.org/data/2.5/forecast/daily\?lat=([A-Za-z0-9+]+)&lon=a&mode=xml&units=metric&cnt=5&APPID=1e38049ae254a0d6c627d624dc13c9fd
			/*
			The Next 1 link has patterns:
			http://api.worldweatheronline.com/free/v1/tz.ashx\?q=([A-Za-z0-9+]+)&format=xml&key=umre32dzkzma6bkwm9k88zgf
			http://api.worldweatheronline.com/free/v1/tz.ashx\?q=a,b&format=xml&key=umre32dzkzma6bkwm9k88zgf
			http://api.worldweatheronline.com/free/v1/tz.ashx\?q=([A-Za-z0-9+]+),a&format=xml&key=umre32dzkzma6bkwm9k88zgf
			Please add these patterns to pack.bundled
			*/
			return pack;
    }
	return pack;
}

var Session_0_1_rex0_0=new RegExp(/http:\/\/lirr42\.mta\.info\/index\.php/);
var Session_0_1_rule = function (link){
    var pack={main:link,bundled:[]};
    Macther_0_1_rex0_0=Session_0_1_rex0_0.exec(link)
    if(Macther_0_1_rex0_0!=null)
    {
			//The incomming link has pattern: http:\/\/lirr42\.mta\.info\/index\.php
			/*The incomming link has parameter list
			RequestAMPM:([A-Za-z0-9+]+)
			RequestDate:([A-Za-z0-9+]+)
			FromStation:([A-Za-z0-9+]+)
			schedules.y:([A-Za-z0-9+]+)
			RequestTime:([A-Za-z0-9+]+)
			sortBy:1
			ToStation:([A-Za-z0-9+]+)
			schedules.x:([A-Za-z0-9+]+)
			*/
			/*
			The Next 1 link has patterns:
			http://lirr42.mta.info/schedules.php
			The Next 1 link has parameters as:
			RequestAMPM:([A-Za-z0-9+]+)
			RequestDate:([A-Za-z0-9+]+)
			FromStation:([A-Za-z0-9+]+)
			schedules.y:([A-Za-z0-9+]+)
			RequestTime:([A-Za-z0-9+]+)
			sortBy:1
			ToStation:([A-Za-z0-9+]+)
			schedules.x:([A-Za-z0-9+]+)
			Please add these patterns and parameters to pack.bundled
			 Number of
			*/
			/*
			Extra Dexription of reuqests
			*/

			return pack;
    }
	return pack;
}
