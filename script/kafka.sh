export SPARK_HOME=/oneapm/local/spark-1.4.1-bin-hadoop2.6/
path=`pwd`/lib
cd $path
ftp.sh -get original-oneapm-spark-streaming-0.1.0-SNAPSHOT.jar
$SPARK_HOME/bin/spark-submit --class com.oneapm.spark.kafka.streaming.SimpleKafkaConsumer --master yarn-client --num-executors 8 --driver-memory 4g --executor-memory 1g --executor-cores 2 --jars $path/fastjson-1.2.5.jar,$path/das-kafka-data-unwrap-2.5.1-SNAPSHOT.jar,$path/spark-streaming-kafka_2.10-1.4.1.jar,$path/oneapm-spark-streaming-0.1.0-SNAPSHOT-dependencies.jar $path/original-oneapm-spark-streaming-0.1.0-SNAPSHOT.jar 10.251.87.214:2181,10.250.236.37:2181,10.104.17.113:2181 10.251.212.218:9092,10.251.214.242:9092 mobile-originallog 60 4 > /oneapm/log/spark/log.`date '+%Y%m%d%H%M'` 2>&1 &

#./bin/spark-submit --class com.oneapm.spark.kafka.streaming.SimpleKafkaConsumer --master yarn-client --num-executors 8 --driver-memory 4g --executor-memory 1g --executor-cores 2 --jars /tmp/fastjson-1.2.5.jar,/tmp/das-kafka-data-unwrap-2.5.1-SNAPSHOT.jar,/tmp/spark-streaming-kafka_2.10-1.4.1.jar /tmp/oneapm-spark-streaming-0.1.0-SNAPSHOT_1.jar 10.251.87.214:2181,10.250.236.37:2181,10.104.17.113:2181 sparkstreaming mobile-originallog 2
#./bin/spark-submit --class LowLevelKafkaConsumer --master yarn-client --num-executors 8 --driver-memory 4g --executor-memory 1g --executor-cores 2 --jars /tmp/kafka-spark-consumer-1.0.3-jar-with-dependencies.jar,/tmp/fastjson-1.2.5.jar,/tmp/das-kafka-data-unwrap-2.5.1-SNAPSHOT.jar /tmp/lowlevelkafkaconsumer_2.10-1.0.jar
