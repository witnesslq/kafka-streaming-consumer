package com.oneapm.spark.kafka

import kafka.serializer.StringDecoder
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.DataFrame
import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka._
import org.apache.phoenix.spark._
import org.apache.hadoop.conf.Configuration

object TpsFormatDataKafkaConsumer {
  def main(args: Array[String]): Unit = {
    if (args.length < 5) {
      System.err.println("Usage: TpsFormatDataKafkaConsumer <HBaseTableName> <zkQuorum> <brokers> <topics> <timeWindow> <numRepartition>")
      System.exit(1)
    }

    val Array(hbaseTable, zkUrl, brokers, topics, timeWindow, numRepartition) = args
    val sparkConf = new SparkConf().setAppName("OneAPM-StreamingKafka for TPS-DC-Metric-formatdata")
    val ssc = new StreamingContext(sparkConf, Seconds(timeWindow.toInt))
    ssc.checkpoint("spark/checkpoint")

    // Kafka configurations
    val kafkaParams = Map[String, String](
      "metadata.broker.list" -> brokers, "serializer.class" -> "kafka.serializer.StringEncoder")

    // Since Spark 1.3 we can use Direct Stream
    val topicsSet = topics.split(",").toSet
    val lines = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, topicsSet)

    // we only need the value of lines since the key is NULL as defined by KafkaDirectStream
    val dataStream = lines.repartition(numRepartition.toInt).map(_._2)

    dataStream.foreachRDD(rdd => {
      val df = RddToDF(rdd)
      println ("##### count before agg ######: " + df.count())
      val aggdf = DfAggregation(df)
      aggdf.show()//TODO remove
      aggdf.printSchema()
      // rename columns to avoid saveToPhoenix failure since DF agg will rename columns automatically
      // the order of the renamed columns really matters! Watch out the order change after agg!
      val aggdf_rename = aggdf.toDF("APPLICATION_ID","TIME_SCOPE","METRIC_TYPE_ID",
        "METRIC_ID","TIME", "AGENT_RUN_ID","NUM2","DATA_VERSION","NUM3","UUID","NUM5",
        "NUM4", "SALT","NUM6", "NUM1")
      aggdf_rename.show()
      println ("##### schema after agg ######: ")
      aggdf_rename.printSchema()
      println ("##### count after agg ######: " + aggdf_rename.count())
      aggdf_rename.saveToPhoenix(hbaseTable, conf = new Configuration(), Some(zkUrl))
    })

    ssc.start()
    ssc.awaitTermination()
  }

  // TODO salt, column name conflict
  def DfAggregation(df: DataFrame): DataFrame = {
    val sqlContext = SQLContextSingleton.getInstance(df.rdd.sparkContext)
    import sqlContext.implicits._
    val aggdf = df.select("DATA_VERSION",
      "SALT","APPLICATION_ID","TIME_SCOPE","METRIC_TYPE_ID",
      "METRIC_ID", "TIME", "AGENT_RUN_ID", "UUID", "NUM1",
      "NUM2", "NUM3", "NUM4", "NUM5", "NUM6").
      groupBy("APPLICATION_ID","TIME_SCOPE","METRIC_TYPE_ID",
               "METRIC_ID", "TIME", "AGENT_RUN_ID").
      agg("DATA_VERSION" -> "min", "SALT" -> "min", "UUID" -> "min", "NUM1"->"sum", "NUM2"->"sum", "NUM3"->"sum",
        "NUM4"->"min", "NUM5"->"max", "NUM6"->"sum")
    aggdf
  }


  def RddToProductRdd(rdd: RDD[String]): RDD[Product] = {
    rdd.map(_.split("\t")).map(r => (r(0).trim.toInt, r(1).trim.toInt, r(2).trim.toInt, r(3).trim.toInt,
      r(4).trim.toLong, r(5).trim.toLong, r(6).trim.toInt, r(7).trim.toInt, r(8).trim.toInt,
      r(9).trim.toFloat, r(10).trim.toFloat, r(11).trim.toFloat,
      r(12).trim.toFloat, r(13).trim.toFloat, r(14).trim.toFloat))
    /*
      toProductRDDFunctions(RddToProductRdd(rdd)).saveToPhoenix("METRIC_DATA_ENTITY_256_TEST", Seq("DATA_VERSION",
                                                          "SALT","APPLICATION_ID","TIME_SCOPE","METRIC_TYPE_ID",
                                                          "METRIC_ID", "TIME", "AGENT_RUN_ID", "UUID", "NUM1",
                                                          "NUM2", "NUM3", "NUM4", "NUM5", "NUM6"),
                                                          conf = new Configuration(), zkUrl2)
                                                          */
  }

  def RddToDF(rdd: RDD[String]): DataFrame = {
    val sqlContext = SQLContextSingleton.getInstance(rdd.sparkContext)
    import sqlContext.implicits._
    val df = rdd.map(_.split("\t")).
      map(r => tpsRecord(r(0).trim.toInt, r(1).trim.toInt, r(2).trim.toInt, r(3).trim.toInt,
      r(4).trim.toLong, r(5).trim.toLong, r(6).trim.toInt, r(7).trim.toInt, r(8).trim.toInt,
      r(9).trim.toFloat, r(10).trim.toFloat, r(11).trim.toFloat,
      r(12).trim.toFloat, r(13).trim.toFloat, r(14).trim.toFloat)).toDF()
    df.show() // TODO remove
    df.printSchema()
    df
  }

  // this is the schema from kafka input stream
  // bigint (phoenix) -> Long, smallint (phoenix) -> Int
  case class tpsRecord(DATA_VERSION: Int, SALT: Int, APPLICATION_ID: Int, TIME_SCOPE: Int,
                       METRIC_TYPE_ID: Long, METRIC_ID: Long, TIME: Int, AGENT_RUN_ID: Int, UUID: Int,
                       NUM1: Float, NUM2: Float, NUM3: Float, NUM4: Float, NUM5: Float, NUM6: Float)

}

