#! /bin/bash
opt=unopt;
netconfig=WIFI;
for i in {1..15}
do

	folder=Result/$opt/$netconfig/com.tapjoy.mytapjoy.instrumented.apk/$i;
	mkdir $folder;
	./runapp.sh $opt $netconfig 	com.tapjoy.mytapjoy.instrumented.apk;
	mv CALL_log $folder;
	mv HTTP_log $folder;
	mv Energy.pt4 $folder;
	mv Energy.txt $folder;
done

if false
then
for i in {1..15}
do

	folder=Result/$opt/$netconfig/com.bobcares.BobsWeather.instrumented.apk/$i;
	mkdir $folder;
	./runapp.sh $opt $netconfig 	com.bobcares.BobsWeather.instrumented.apk;
	mv CALL_log $folder;
	mv HTTP_log $folder;
	mv Energy.pt4 $folder;
	mv Energy.txt $folder;
done
for i in {1..15}
do

	folder=Result/$opt/$netconfig/com.salman.lirrschedule.instrumented.apk/$i;
	mkdir $folder;
	./runapp.sh $opt $netconfig 	com.salman.lirrschedule.instrumented.apk;
	mv CALL_log $folder;
	mv HTTP_log $folder;
	mv Energy.pt4 $folder;
	mv Energy.txt $folder;
done
for i in {1..15}
do

	folder=Result/$opt/$netconfig/com.tapjoy.mytapjoy.instrumented.apk/$i;
	mkdir $folder;
	./runapp.sh $opt $netconfig 	com.tapjoy.mytapjoy.instrumented.apk;
	mv CALL_log $folder;
	mv HTTP_log $folder;
	mv Energy.pt4 $folder;
	mv Energy.txt $folder;
done


for i in {1..15}
do

	folder=Result/$opt/$netconfig/net.aljazeera.tablet.english.instrumented.apk/$i;
	mkdir $folder;
	./runapp.sh $opt $netconfig 	net.aljazeera.tablet.english.instrumented.apk;
	mv CALL_log $folder;
	mv HTTP_log $folder;
	mv Energy.pt4 $folder;
	mv Energy.txt $folder;
done

for i in {1..15}
do

	folder=Result/$opt/$netconfig/pch.apps.pchsweeps.instrumented.apk/$i;
	mkdir $folder;
	./runapp.sh $opt $netconfig pch.apps.pchsweeps.instrumented.apk;
	mv CALL_log $folder;
	mv HTTP_log $folder;
	mv Energy.pt4 $folder;
	mv Energy.txt $folder;
done
fi

