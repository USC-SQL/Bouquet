#! /bin/bash
currentpath=`pwd`

f=$1;
rm -r sootOutput
fname=`basename $f`;
casename=`echo ${fname/.apk/}`
java -jar soot.jar -android-jars $currentpath/libs -src-prec apk -f J -process-dir ${casename}.apk -allow-phantom-refs
rm ${casename}.apk
cd sootOutput
ls | sed -e "s/.jimple//g" > ../classlists/$casename.txt
