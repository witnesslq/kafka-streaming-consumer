package com.caishi.kafka.customer

import java.util.Calendar
import java.text.SimpleDateFormat

import kafka.serializer.StringDecoder
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{SaveMode, SQLContext}
import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka._
import org.apache.spark.{SparkContext, SparkConf}

/**
  * Created by root on 15-10-12.
  * kafka consumer real time to hdfs
  */
object Common {
  def main(args: Array[String]): Unit = {
    if (args.length < 7) {
      System.err.println("Usage: Common <brokers> <topics> <timeWindow> <numRepartition> <autooffset> <groupId> <pathPre:hdfs pre >")
      System.exit(1)
    }

    val Array(brokers, topics, timeWindow, numRepartition,autooffset,groupId,pathPre) = args
    //    offline zk: 10.10.42.24:2181,10.10.42.25:2128,10.10.42.24:2128
    //    val zkQuorum:String = "192.168.100.63:2181,192.168.100.64:2181,192.168.100.65:2181"
    //    val brokers : String = "192.168.100.70:9092,192.168.100.70:9092,192.168.100.70:9092"
    //    val topics : String = "topic-user-info,topic-user-active"
    //    val topics : String = "topic-user-active"
    //    val timeWindow : Int = 20
    //    val numRepartition : Int = 2
    //    val pathPre : String ="hdfs://192.168.100.73:9000/test/dw"
    //    val autooffset= "largest" //smallest
    //    val groupId = "spark-streaming-test"
    val sparkConf = new SparkConf().setAppName("spark-streaming-log")
    sparkConf.set("spark.streaming.kafka.maxRatePerPartition","10000")
    val ssc = new StreamingContext(sparkConf, Seconds(timeWindow.toInt))

    //    ssc.sparkContext.setLocalProperty("spark.scheduler.pool","production")
    // Kafka configurations
    val kafkaParams = Map[String, String](
      "metadata.broker.list" -> brokers,
      "serializer.class" -> "kafka.serializer.StringEncoder",
      "group.id" -> groupId,
      "auto.offset.reset" -> autooffset
    )

    // Since Spark 1.3 we can use Direct Stream
    val topicsSet = topics.split(",").toSet
    //    val tm = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, topicsSet)
    val km = new KafkaManager(kafkaParams)
    val data = km.createDirectStream[String, String, StringDecoder, StringDecoder](ssc,kafkaParams,topicsSet)
    // json to K/V
    val lines = data.map(_._2)
    lines.filter(checkDataType2(_,"topic-user-info")).foreachRDD(rdd => saveToParquet(rdd,"topic-user-info",pathPre))
    lines.filter(checkDataType2(_,"topic-user-active")).foreachRDD(rdd => saveToParquet(rdd,"topic-user-active",pathPre))
    lines.filter(checkDataType2(_,"topic-user-course-subscribe")).foreachRDD(rdd => saveToParquet(rdd,"topic-user-course-subscribe",pathPre))
    lines.filter(checkDataType2(_,"topic-user-workout-finished")).foreachRDD(rdd => saveToParquet(rdd,"topic-user-workout-finished",pathPre))
    //    更新kafka 监控offset值
    data.foreachRDD(rdd => {
      if(!rdd.isEmpty()){
        //更新zk offset
        km.updateZKOffsets(rdd)
      }
    })
    ssc.start()
    ssc.awaitTermination()
  }

  def checkDataType2(d : String,dataType:String): Boolean ={
    val s = com.alibaba.fastjson.JSON.parseObject(d).get("topic")
    val isType = dataType.equals(s)
    isType
  }

  def saveToParquet(rdd: RDD[String]): Unit = {
    val sqlContext = SQLContextSingleton.getInstance(rdd.sparkContext)
    sqlContext.setConf("parquet.enable.summary-metadata", "false")

    // Loads an `JavaRDD[String]` storing JSON objects (one object per record)
    val df = sqlContext.read.json()
    if(df.count() > 0){
      try {
        df.write.format("parquet").mode(SaveMode.Append).save("hdfs://192.168.100.73:9000/test/test")
      }catch {
        case e: Throwable =>
          println("ERROR: Save to parquet error\n" + e.toString + "\n" + rdd.collect())
      }
    }
  }

  def saveToParquet(rdd: RDD[String], dataType: String,pathPre : String): Unit = {
    val sqlContext = SQLContextSingleton.getInstance(rdd.sparkContext)
    sqlContext.setConf("parquet.enable.summary-metadata", "false")

    // Loads an `JavaRDD[String]` storing JSON objects (one object per record)
    val df = sqlContext.read.json(rdd)
    //    if(df.count() > 0){
    val dirname = dirName.getDirName(pathPre,dataType)
    try {
      df.write.format("parquet").mode(SaveMode.Append).save(dirname)
    }catch {
      case e: Throwable =>
        println("ERROR: Save to parquet error\n" + e.toString + "\n" + rdd.collect())
    }
    //    }
  }
}

/* 计算文件存放目录 */
object dirName {
  def getDirName(pathPre : String, dataType: String): String = {
    val time = Calendar.getInstance().getTime
    val year = new SimpleDateFormat("yyyy").format(time)
    val month = new SimpleDateFormat("MM").format(time)
    val day = new SimpleDateFormat("dd").format(time)
    val hour = new SimpleDateFormat("HH").format(time)
    val minute = new SimpleDateFormat("mm").format(time)
    val filename = (pathPre+"/%s/%s/%s/" + dataType + "/%s").format(year, month, day, hour)
    filename
  }
}

/** Lazily instantiated singleton instance of SQLContext */
object SQLContextSingleton {
  @transient  private var instance: SQLContext = _
  def getInstance(sparkContext: SparkContext): SQLContext = {
    if (instance == null) {
      instance = new SQLContext(sparkContext)
      instance.setConf("spark.sql.parquet.compression.codec", "snappy")
    }
    instance
  }
}

