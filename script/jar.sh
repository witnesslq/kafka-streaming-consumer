path=`pwd`
cd $path/../target
rm -rf tmp
mkdir tmp
cd tmp
jar xf ../oneapm-spark-streaming-0.1.0-SNAPSHOT.jar
rm -rf com/oneapm
cd ..
jar -cf oneapm-spark-streaming-0.1.0-SNAPSHOT-dependencies.jar -C tmp/ . 
rm -rf tmp
ftp.sh -put oneapm-spark-streaming-0.1.0-SNAPSHOT-dependencies.jar
