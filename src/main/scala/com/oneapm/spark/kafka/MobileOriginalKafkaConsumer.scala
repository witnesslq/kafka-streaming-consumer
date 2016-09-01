package com.oneapm.spark.kafka

import java.util.Calendar
import java.text.SimpleDateFormat

import com.alibaba.fastjson.JSON
import com.blueocn.das.kafa.data.unwrap.LogData
import com.blueocn.das.web.agent.model.crashv2.AndroidCrashData
import com.blueocn.das.web.agent.model.{MobileData, ConnectInformation}
import kafka.serializer.StringDecoder
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming._
import org.apache.spark.sql.{SaveMode, SQLContext}
import org.apache.spark.streaming.kafka._
import org.apache.spark.{SparkContext, SparkConf}

object MobileOriginalKafkaConsumer {
  def main(args: Array[String]): Unit = {
    if (args.length < 5) {
      System.err.println("Usage: MobileOriginalKafkaConsumer <zkQuorum> <brokers> <topics> <timeWindow> <numRepartition>")
      System.exit(1)
    }

    //StreamingExamples.setStreamingLogLevels()

    val Array(zkQuorum, brokers, topics, timeWindow, numRepartition) = args
    val sparkConf = new SparkConf().setAppName("OneAPM-StreamingKafka for Mi Original Log")
    val ssc = new StreamingContext(sparkConf, Seconds(timeWindow.toInt))
    ssc.checkpoint("spark/checkpoint") //TODO what's going on here?

    // Kafka configurations
    val kafkaParams = Map[String, String](
      "metadata.broker.list" -> brokers, "serializer.class" -> "kafka.serializer.StringEncoder")

    // Since Spark 1.3 we can use Direct Stream
    val topicsSet = topics.split(",").toSet
    val lines = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, topicsSet)

    def headerMatch(record: String, target: String): Boolean = {
      val data = new LogData(record.toString)
      val header = data.getHeader
      target.equals(header.getDataType)
    }

    def getJson(record: String, classNum: Int): String = {
      try {
        val data = new LogData(record)
        val body = {
          classNum match {
            case 1 => data.getBody(classOf[ConnectInformation])
            case 2 => data.getBody(classOf[MobileData])
            case 3 => data.getBody(classOf[AndroidCrashData])
          }
        }
        JSON.toJSONString(body, true)
      }
        catch {
        case e: Throwable =>
          println("ERROR: Json from kakfa is broken\n" + e.toString)
          println(record)
          null
      }
    }

    // we only need the value of lines since the key is NULL as defined by KafkaDirectStream
    val connectDataJson = lines.repartition(numRepartition.toInt).map(_._2).filter(headerMatch(_,"ConnectData")).map(record => {
      getJson(record, 1)
    })
    val mobileDataJson = lines.repartition(numRepartition.toInt).map(_._2).filter(headerMatch(_,"Data")).map(record => {
      getJson(record, 2)
    })
    val andCrashDataJson = lines.repartition(numRepartition.toInt).map(_._2).filter(headerMatch(_,"AndroidCrashData")).map(record => {
      getJson(record, 3)
    })

    // FIXME all printout here will happen in Driver which is useless!
    // json.print(1) // print 1 elements in every batch
    // json.map(_._1).print() // print 10 elements in every batch
    // println("$$$ dirname = " + json.map(_._2).print()) //"dirname" is printed once, but json_2 is print 10 elements every batch

    connectDataJson.foreachRDD(rdd => {
      saveToParquet(rdd, "mi-originallog-connectdata-pr")
    })
    mobileDataJson.foreachRDD(rdd => {
      saveToParquet(rdd, "mi-originallog-mobiledata-prq")
    })
    andCrashDataJson.foreachRDD(rdd => {
      saveToParquet(rdd, "mi-originallog-androidcrash-prq")
    })

    def saveToParquet(rdd: RDD[String], dataType: String): Unit = {
      val sqlContext = SQLContextSingleton.getInstance(rdd.sparkContext)
      import sqlContext.implicits._
      // Loads an `JavaRDD[String]` storing JSON objects (one object per record)
      val df = sqlContext.read.json(rdd)
      val dirname = {
        dataType match {
          case "mi-originallog-connectdata-pr" =>
            dirName.getDirName("mi-originallog-connectdata-prq")
          case "mi-originallog-mobiledata-prq" =>
            dirName.getDirName("mi-originallog-mobiledata-prq")
          case "mi-originallog-androidcrash-prq" =>
            dirName.getDirName("mi-originallog-androidcrash-prq")
        }
      }
      try
      //df.write.format("parquet").save(dirname)
      df.write.format("parquet").mode(SaveMode.Append).save(dirname)
      catch {
        case e: Throwable =>
          println("ERROR: Save to parquet error\n" + e.toString + "\n" + rdd.collect())
      }
    }

    ssc.start()
    ssc.awaitTermination()
  }
}

/* Object Initialized when called to generate timestamp from time to time*/
object dirName {
  def getDirName(dataType: String): String = {
    val time = Calendar.getInstance().getTime
    val year = new SimpleDateFormat("yyyy").format(time)
    val month = new SimpleDateFormat("MM").format(time)
    val day = new SimpleDateFormat("dd").format(time)
    val hour = new SimpleDateFormat("HH").format(time)
    val minute = new SimpleDateFormat("mm").format(time)
    val filename = ("/test/dw/%s/%s/%s/" + dataType + "/%s").
      format(year, month, day, hour)
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