#! /bin/bash
adb pull /sdcard/CALL_log;
adb pull /sdcard/HTTP_log;
adb shell rm /sdcard/CALL_log;
adb shell rm /sdcard/HTTP_log;

curl --data "user=SQLmember" http://192.168.43.63/PowerMonitor/stop.php >Energy.pt4;
./Pt4Reader.exe Energy.pt4 >Energy.txt;
java -jar ResultReader.jar ./CALL_log ./Energy.txt;
