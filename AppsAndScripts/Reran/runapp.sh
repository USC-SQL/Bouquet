#! /bin/bash
curl --data "user=SQLmember" http://192.168.1.8/PowerMonitor/start.php
sleep 5;
filename=${3%.apk};
optpath=$1;
netpath=$2;

adb install -r InstrumentedNew/$optpath/$3;
sleep 5;
adb push Script/$netpath/$filename.scp /data/local;
adb shell rm /sdcard/CALL_log;
adb shell rm /sdcard/HTTP_log;
adb shell /data/local/replay.exe /data/local/$filename.scp;
adb install -r InstrumentedNew/$optpath/$3;
sleep 5;
adb pull /sdcard/CALL_log;
adb pull /sdcard/HTTP_log;
adb shell rm /sdcard/CALL_log;
adb shell rm /sdcard/HTTP_log;

curl --data "user=SQLmember" http://192.168.1.8/PowerMonitor/stop.php >Energy.pt4;
./Pt4Reader.exe Energy.pt4 >Energy.txt;
