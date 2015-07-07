#! /bin/bash
if [ $# -ne 2 ]
then
 echo "usage: instrumenter app opt"
 exit;
fi
rm -r tempfolder;
mkdir tempfolder;
apktool d -s $1 resource;
filename=${1%.apk}
./addpermission.pl resource/AndroidManifest.xml #add the write permission
mv TEMP resource/AndroidManifest.xml
mv resource tempfolder/resource;
cd tempfolder;
cp resource/classes.dex ./classes_backup.dex
mv resource/classes.dex ./classes.dex
dex2jar classes.dex 
rm classes.dex
mkdir classes
unzip  classes-dex2jar.jar -d classes
#mv bin classes
java -jar ../HTTPMerger.jar classes true $2
cp -r ../javalib/* outPut/
#mv ./bin/epp.jar ./classes; 
dx --dex --verbose --output=classes.dex outPut
mv classes.dex resource;
apktool b resource temp.apk;

jarsigner -sigalg SHA1withRSA -digestalg SHA1 -verbose -keystore ../wendy.keystore -storepass USCDING -signedjar ${filename}.instrumented.apk temp.apk wendy.keystore;
mv ${filename}.instrumented.apk ../
