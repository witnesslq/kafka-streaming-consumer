package com.caishi.kafka.customer

import java.util.Calendar
import java.text.SimpleDateFormat

import kafka.serializer.StringDecoder
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.functions.udf
import org.apache.spark.sql.{SQLContext, SaveMode}
import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka._
import org.apache.spark.{SparkConf, SparkContext}
/**
  * Created by root on 15-10-12.
  * kafka consumer real time to hdfs
  */
object Common {
  var pathPre = ""
  def main(args: Array[String]): Unit = {
//    if (args.length < 7) {
//      System.err.println("Usage: Common <brokers> <topics> <timeWindow> <numRepartition> <autooffset> <groupId> <pathPre:hdfs pre >")
//      System.exit(1)
//    }
//
//    val Array(brokers, topics, timeWindow, numRepartition,autooffset,groupId,pathPre) = args
        val brokers : String = "192.168.100.70:9092,192.168.100.71:9092,192.168.100.72:9092"
        val topics : String = "topic-user-browse,topic-user-openapp"
//        val topics : String = "topic-user-active"
        val timeWindow : Int = 5
        val numRepartition : Int = 2
        val pathPre : String ="hdfs://192.168.100.73:9000/test/dw"
        val autooffset= "largest" //smallest
        val groupId = "spark-streaming-test-test"
    val sparkConf = new SparkConf().setAppName("spark-streaming-log").setMaster("local")
    sparkConf.set("spark.streaming.kafka.maxRatePerPartition","10000")
    sparkConf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
    val ssc = new StreamingContext(sparkConf, Seconds(timeWindow.toInt))

    this.pathPre = pathPre

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
    val lines = data.map(_._2).filter(!"".equals(_))
//    lines.filter(checkDataType2(_,"topic-user-info")).foreachRDD(rdd => saveToParquet(rdd,"topic-user-info",pathPre))
//    lines.filter(checkDataType2(_,"topic-user-active")).foreachRDD(rdd => saveToParquet(rdd,"topic-user-active",pathPre))
//    lines.filter(checkDataType2(_,"topic-user-course-subscribe")).foreachRDD(rdd => saveToParquet(rdd,"topic-user-course-subscribe",pathPre))
//    lines.filter(checkDataType2(_,"topic-user-course-unsubscribe")).foreachRDD(rdd => saveToParquet(rdd,"topic-user-course-unsubscribe",pathPre))
//    lines.filter(checkDataType2(_,"topic-user-workout-finished")).foreachRDD(rdd => saveToParquet(rdd,"topic-user-workout-finished",pathPre))
//    lines.filter(checkDataType2(_,"topic-user-login")).foreachRDD(rdd => saveToParquet(rdd,"topic-user-login",pathPre))
//    lines.filter(checkDataType2(_,"topic-user-openapp")).foreachRDD(rdd => saveToParquet(rdd,"topic-user-openapp",pathPre))
//    lines.filter(checkDataType2(_,"topic-user-quitapp")).foreachRDD(rdd => saveToParquet(rdd,"topic-user-quitapp",pathPre))
//    lines.filter(checkDataType2(_,"topic-user-browse")).map(Util.convertToJson(_,4)).foreachRDD(rdd => saveToParquet(rdd,"topic-user-browse",pathPre))
//    lines.filter(checkDataType2(_,"topic-user-media")).foreachRDD(rdd => saveToParquet(rdd,"topic-user-media",pathPre))
//    lines.filter(checkDataType2(_,"topic-user-course-option")).foreachRDD(rdd => saveToParquet(rdd,"topic-user-course-option",pathPre))
//    lines.filter(checkDataType2(_,"topic-user-search")).foreachRDD(rdd => saveToParquet(rdd,"topic-user-search",pathPre))
//
//    lines.filter(checkDataType2(_,"topic-mall-order")).map(Util.convertToJson(_,1)).foreachRDD(rdd => saveToParquet(rdd,"topic-mall-order",pathPre))
//    lines.filter(checkDataType2(_,"topic-mall-pay")).map(Util.convertToJson(_,2)).foreachRDD(rdd => saveToParquet(rdd,"topic-mall-pay",pathPre))
//    lines.filter(checkDataType2(_,"topic-mall-browse")).map(Util.convertToJson(_,3)).foreachRDD(rdd => saveToParquet(rdd,"topic-mall-browse",pathPre))

    lines.map(d => (getTopic(d), d)).groupByKey().foreachRDD(saveToParquet2(_))
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

  def getTopic(json : String): String ={
    val s = com.alibaba.fastjson.JSON.parseObject(json).get("topic").toString
    s
  }
  def saveToParquet2(rdd: RDD[(String,Iterable[String])]): Unit = {
    val sqlContext = SQLContextSingleton.getInstance(rdd.sparkContext)
    sqlContext.setConf("parquet.enable.summary-metadata", "false")

    rdd.map(kv =>{
      // Loads an `JavaRDD[String]` storing JSON objects (one object per record)
      val df = sqlContext.read.json(rdd.flatMap(_._2))
      df.printSchema()

      //      val toSSS = udf( _ : String  => com.alibaba.fastjson.JSON)
      val toStr = udf[AnyRef,String](_.toString)
      println(df.count())
      if (df.count() > 0) {
        df.show(100,false)
        df.withColumn("data",toStr(df("data"))).printSchema()
        df.printSchema()
        val dirname = dirName.getDirName(pathPre, kv._1)
        try {
          df.write.format("parquet").mode(SaveMode.Append).save(dirname)
        } catch {
          case e: Throwable =>
            println("ERROR: Save to parquet error\n" + e.toString + "\n" + rdd.collect())
        }
      }
    })

  }

  def saveToParquet(rdd: RDD[String], dataType: String,pathPre : String): Unit = {
    val sqlContext = SQLContextSingleton.getInstance(rdd.sparkContext)
    sqlContext.setConf("parquet.enable.summary-metadata", "false")

    // Loads an `JavaRDD[String]` storing JSON objects (one object per record)
    val df = sqlContext.read.json(rdd)
    if(df.count() > 0){
      val dirname = dirName.getDirName(pathPre,dataType)
      try {
        df.write.format("parquet").mode(SaveMode.Append).save(dirname)
      }catch {
        case e: Throwable =>
          println("ERROR: Save to parquet error\n" + e.toString + "\n" + rdd.collect())
      }
    }
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
    val filename = (pathPre+"/%s/%s/%s/" + dataType +"/%s").format(year, month, day,hour)
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

