package com.caishi.kafka.customer

import java.util.Calendar
import java.text.SimpleDateFormat

import kafka.serializer.StringDecoder
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{SaveMode, SQLContext}
import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka._
import org.apache.spark.{SparkContext, SparkConf}
import com.fitttime.kafka.model.Util
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
//        val brokers : String = "192.168.100.56:9092"
//        val topics : String = "topic-user-browse,topic-user-openapp"
////        val topics : String = "topic-user-active"
//        val timeWindow : Int = 5
//        val numRepartition : Int = 2
//        val pathPre : String ="hdfs://192.168.100.73:9000/test/dw"
//        val autooffset= "largest" //smallest
//        val groupId = "spark-streaming-test-test"
    val sparkConf = new SparkConf().setAppName("spark-streaming-log")
    sparkConf.set("spark.streaming.kafka.maxRatePerPartition","10000")
    sparkConf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
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
      try {
          val lines = data.map(_._2).filter(!"".equals(_))

          // 新的app数据不做分流,直接落入文件
          lines.filter(checkNewDataType).foreachRDD(rdd => saveToParquet2(rdd,"topic-app-data",pathPre))

          lines.filter(checkDataType2(_,"topic-user-info")).foreachRDD(rdd => saveToParquet(rdd,"topic-user-info",pathPre))
          lines.filter(checkDataType2(_,"topic-user-active")).foreachRDD(rdd => saveToParquet(rdd,"topic-user-active",pathPre))
          lines.filter(checkDataType2(_,"topic-user-course-subscribe")).foreachRDD(rdd => saveToParquet(rdd,"topic-user-course-subscribe",pathPre))
          lines.filter(checkDataType2(_,"topic-user-course-unsubscribe")).foreachRDD(rdd => saveToParquet(rdd,"topic-user-course-unsubscribe",pathPre))
          lines.filter(checkDataType2(_,"topic-user-workout-finished")).foreachRDD(rdd => saveToParquet(rdd,"topic-user-workout-finished",pathPre))
          lines.filter(checkDataType2(_,"topic-user-login")).foreachRDD(rdd => saveToParquet(rdd,"topic-user-login",pathPre))
          lines.filter(checkDataType2(_,"topic-user-openapp")).foreachRDD(rdd => saveToParquet(rdd,"topic-user-openapp",pathPre))
          lines.filter(checkDataType2(_,"topic-user-quitapp")).foreachRDD(rdd => saveToParquet(rdd,"topic-user-quitapp",pathPre))
          lines.filter(checkDataType2(_,"topic-user-browse")).map(Util.convertToJson(_,4)).foreachRDD(rdd => saveToParquet(rdd,"topic-user-browse",pathPre))
          lines.filter(checkDataType2(_,"topic-user-media")).foreachRDD(rdd => saveToParquet(rdd,"topic-user-media",pathPre))
          lines.filter(checkDataType2(_,"topic-user-course-option")).foreachRDD(rdd => saveToParquet(rdd,"topic-user-course-option",pathPre))
          lines.filter(checkDataType2(_,"topic-user-search")).foreachRDD(rdd => saveToParquet(rdd,"topic-user-search",pathPre))
          lines.filter(checkDataType2(_,"topic-user-mealplan-checkin")).foreachRDD(rdd => saveToParquet(rdd,"topic-user-mealplan-checkin",pathPre))
          lines.filter(checkDataType2(_,"topic-user-mealplan")).foreachRDD(rdd => saveToParquet(rdd,"topic-user-mealplan",pathPre))

          lines.filter(checkDataType2(_,"topic-mall-order")).map(Util.convertToJson(_,1)).foreachRDD(rdd => saveToParquet(rdd,"topic-mall-order",pathPre))
          lines.filter(checkDataType2(_,"topic-mall-pay")).map(Util.convertToJson(_,2)).foreachRDD(rdd => saveToParquet(rdd,"topic-mall-pay",pathPre))
          lines.filter(checkDataType2(_,"topic-mall-browse")).map(Util.convertToJson(_,3)).foreachRDD(rdd => saveToParquet(rdd,"topic-mall-browse",pathPre))
      }catch {
          case ex: Exception =>{
              println("project error:",ex.printStackTrace())
          }
      }

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
        var isType = false
        try{
            val s = com.alibaba.fastjson.JSON.parseObject(d).get("topic")
            isType = dataType.equals(s)
        }catch {
            case e: Throwable =>
                println("ERROR: parse json string:\n" + e.toString + "\n")
        }
        isType
    }
    // 对app新版数据不进行分流,都放在一个目录下
    def checkNewDataType(d : String): Boolean ={
        var isType = false
        try{
            val s = com.alibaba.fastjson.JSON.parseObject(d).get("topic")

            if (s != null)
                isType = s.toString.toLowerCase().startsWith("topic-app-")
        }catch {
            case e: Throwable =>
                println("ERROR: parse json string:\n" + e.toString + "\n")
        }
        isType
    }

    def saveToParquet2(rdd: RDD[String], dataType: String,pathPre : String): Unit = {
        val sqlContext = SQLContextSingleton.getInstance(rdd.sparkContext)
        sqlContext.setConf("parquet.enable.summary-metadata", "false")
        import sqlContext.implicits._
        val df = rdd.toDF("data")
        // Loads an `JavaRDD[String]` storing JSON objects (one object per record)
//        val df = sqlContext.read.json(rdd)
        if(df.count() > 0){
            val dirname = dirName.getDirName(pathPre,dataType)
            try {
                df.write.format("parquet").option("mergeSchema", "true").mode(SaveMode.Append).save(dirname)
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
    if(df.count() > 0){
      val dirname = dirName.getDirName(pathPre,dataType)
      try {
        df.write.format("parquet").option("mergeSchema", "true").mode(SaveMode.Append).save(dirname)
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

