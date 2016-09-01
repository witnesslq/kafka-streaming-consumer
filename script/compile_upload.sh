path=`pwd`
cd $path/../
mvn32 clean package
cd target
ftp.sh -put original-oneapm-spark-streaming-0.1.0-SNAPSHOT.jar
