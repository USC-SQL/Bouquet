#! /bin/bash
adb pull /sdcard/CALL_log
adb pull /sdcard/HTTP_log
adb shell rm /sdcard/CALL_log
adb shell rm /sdcard/HTTP_log
#rm -r tempfolder
