//bobweather
var regexbob0=new RegExp(/http:\/\/api.openweathermap.org\/data\/2\.5\/weather\?q=([A-Za-z0-9\+]+)&mode=xml&units=metric&APPID=1e38049ae254a0d6c627d624dc13c9fd/);
var regexbob1=new RegExp(/http:\/\/api.openweathermap.org\/data\/2.5\/forecast\/daily\?q=([A-Za-z0-9\+]+)&mode=xml&units=metric&cnt=5&APPID=1e38049ae254a0d6c627d624dc13c9fd/);
var regexbob2=new RegExp(/http:\/\/api.worldweatheronline.com\/free\/v1\/tz.ashx\?q=([A-Za-z0-9\+]+)&format=xml&key=umre32dzkzma6bkwm9k88zgf/);
//LIRR
var regexLIRR0=new RegExp(/http:\/\/lirr42.mta.info\/index.php/);
var regexLIRR1=new RegExp(/http:\/\/lirr42.mta.info\/index.php\?FromStation=([A-Za-z0-9\+]+)&ToStation=([A-Za-z0-9\+]+)&RequestDate=([A-Za-z0-9\%\+]+)&RequestTime=([A-Za-z0-9\%\+]+)&RequestAMPM=([A-Za-z0-9\%\+]+)&sortBy=([A-Za-z0-9\%\+]+)&schedules.x=([A-Za-z0-9\%\+]+)&schedules.y=([A-Za-z0-9\%\+]+)/);
var regexLIRR2=new RegExp(/http:\/\/lirr42.mta.info\/schedules.php\?FromStation=([A-Za-z0-9\+]+)&ToStation=([A-Za-z0-9\+]+)&RequestDate=([A-Za-z0-9%\+]+)&RequestTime=([A-Za-z0-9%\+]+)&RequestAMPM=([A-Za-z0-9%\+]+)&sortBy=([A-Za-z0-9%\+]+)&schedules.x=([A-Za-z0-9%\+]+)&schedules.y=([A-Za-z0-9%\+]+)/);
//ALJA
var regexALJA0=new RegExp(/http:\/\/mis-aljazeera.gos2m.nl\/MisLogM2Auth\/aljazeera\/at\/([A-Za-z0-9\+]+)\/([A-Za-z0-9\+]+)\/startup\?store=(android)&version=1.1.0/);
var regexALJA1=new RegExp(/http:\/\/mis-aljazeera.gos2m.nl\/MisLogM2Auth\/aljazeera\/at\/([A-Za-z0-9\+]+)\/([A-Za-z0-9\+]+)\/startup\?store=(android)&version=1.1.0/);
//Tapjoy
var regexTapjoy0=new RegExp(/https:\/\/ws.tapjoyads.com\/videos\?.+/);
var regexTapjoy1=new RegExp(/https:\/\/ws.tapjoyads.com\/connect\?.+/);
var regexTapjoy2=new RegExp(/https:\/\/s3.amazonaws.com\/tapjoy\/videos\/assets\/watermark.png/);
//pch
var regexpch0=new RegExp(/https:\/\/ws.tapjoyads.com\/videos\?.+/);
var regexpch1=new RegExp(/https:\/\/connect.tapjoy.com\/connect\?.+/);
var regexpch2=new RegExp(/http:\/\/appclick.co\/PublicServices\/MobileTrackingApiRestV1\.svc\/AppWasRunV2\/Put.*/);
//funnypic
var regfunnypic0=new RegExp(/http:\/\/ad\.leadboltapps\.net\/show_app\.conf\?.*/);
var regfunnypic1=new RegExp(/http:\/\/ad\.leadboltapps\.net\/show_reengagement\?.*/);
var regfunnypic2=new RegExp(/http:\/\/api\.appfireworks\.com.*/);
//troll face
var regtrollface2=new RegExp(/http:\/\/ad\.leadboltapps\.net\/show_app_audio.*/);

var regs=[];
regs.push({reg:regexbob0,file:"stored/bobweather/0/"});
regs.push({reg:regexbob1,file:"stored/bobweather/1/"});
regs.push({reg:regexbob2,file:"stored/bobweather/2/"});
regs.push({reg:regexLIRR0,file:"stored/LIRR/0/"});
regs.push({reg:regexLIRR1,file:"stored/LIRR/1/"});
regs.push({reg:regexLIRR2,file:"stored/LIRR/2/"});
regs.push({reg:regexALJA0,file:"stored/ALJA/0/"});
regs.push({reg:regexALJA1,file:"stored/ALJA/1/"});
regs.push({reg:regexTapjoy0,file:"stored/tapjoy/0/"});
regs.push({reg:regexTapjoy1,file:"stored/tapjoy/1/"});
regs.push({reg:regexTapjoy2,file:"stored/tapjoy/2/"});
regs.push({reg:regexpch0,file:"stored/pch/0/"});
regs.push({reg:regexpch1,file:"stored/pch/1/"});
regs.push({reg:regexpch2,file:"stored/pch/4/"});
regs.push({reg:regfunnypic0,file:"stored/funnypic/7/"});
regs.push({reg:regfunnypic1,file:"stored/funnypic/0/"});
regs.push({reg:regfunnypic2,file:"stored/funnypic/8/"});
regs.push({reg:regtrollface2,file:"stored/trollface/0"});

module.exports.regulars=regs;
module.exports.lookup = function(key){
	var r=null;
	regs.forEach(function (obj){
		if(obj.reg.exec(key)!=null)
		{
			r=obj;
		}

	})
	return r;
}
