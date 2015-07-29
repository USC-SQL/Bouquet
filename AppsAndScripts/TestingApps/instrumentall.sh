#! /bin/bash
for file in GoodBoys/*.apk
do 
fname=`basename $file`;
cp $file ./
./instrumentor.sh $fname  false;
done
