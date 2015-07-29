java -jar OrderEvents.jar recordedEvents.txt
java -jar translate.jar recordedEvents_ordered.txt translatedEvents.txt
adb -s 192.168.1.5:5555 push translatedEvents.txt /data/local
