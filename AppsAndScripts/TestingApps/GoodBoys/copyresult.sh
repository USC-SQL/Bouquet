#! /bin/bash
for file in *.apk
do
txtfile=${file%.apk};
cp ../GoodResult/$txtfile.txt ./
echo $txtfile.txt
done

