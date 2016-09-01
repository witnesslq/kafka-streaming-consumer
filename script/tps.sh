export SPARK_HOME=/oneapm/local/spark-1.4.1-bin-hadoop2.6/
path=`pwd`/lib
cd $path
ftp.sh -get original-oneapm-spark-streaming-0.1.0-SNAPSHOT.jar
#ftp.sh -get oneapm-spark-streaming-0.1.0-SNAPSHOT.jar
filename=log.tps.`date '+%Y%m%d%H%M'`
$SPARK_HOME/bin/spark-submit --conf "spark.driver.extraClassPath=/home/hadoop/spark/lib/hbase-protocol-1.0.1.1.jar" --conf "spark.executor.extraClassPath=/oneapm/local/phoenix-4.4.0-HBase-1.0-bin/phoenix-4.4.0-HBase-1.0-client.jar" --class com.oneapm.spark.kafka.TpsFormatDataKafkaConsumer --master yarn-client --num-executors 8 --driver-memory 4g --executor-memory 1g --executor-cores 2 --jars $path/hbase-protocal-1.0.1.1.jar,$path/spark-streaming-kafka_2.10-1.4.1.jar,$path/oneapm-spark-streaming-0.1.0-SNAPSHOT-dependencies.jar $path/original-oneapm-spark-streaming-0.1.0-SNAPSHOT.jar METRIC_DATA_ENTITY_256_AGG 10.172.171.229:2181,10.171.84.196:2181,10.170.197.109:2181,10.172.177.98:2181,10.171.22.201:2181:/hbase_online 10.251.210.240:9092,10.251.213.182:9092,10.251.208.216:9092,10.251.211.207:9092 tps-dc-metric-formatdata 60 8 > /oneapm/log/spark/$filename 2>&1 &
sleep 3
cd ..
tail -F log/$filename
