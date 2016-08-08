package com.fitttime.kafka.model

import java.util.Properties

import org.apache.spark.sql.{Row, SQLContext}
import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}
import org.apache.spark.{SparkConf, SparkContext}
/**
 * Created by root on 15-10-12.
 */
object Test_Sql_Mysql {
  def main(args: Array[String]) {
    val sparkConf = new SparkConf().setAppName("read-mysql").setMaster("local")
    val sc = new SparkContext(sparkConf)
    val ssc = new SQLContext(sc)
    var p = Map("url" -> "jdbc:mysql://192.168.100.254:3306/report",
      "driver" -> "com.mysql.jdbc.Driver",
    "dbtable" -> "click_statistics",
    "user"->"root",
    "password" -> "Bigdata000000")
    val df = ssc.read.format("jdbc").options(p).load()
    df.show(10,true)
  }

  def write: Unit = {
    val sparkConf = new SparkConf().setAppName("read-mysql").setMaster("local")
    val sc = new SparkContext(sparkConf)
    val ssc = new SQLContext(sc)
    var p = Map("url" -> "jdbc:mysql://192.168.100.254:3306/report",
      "driver" -> "com.mysql.jdbc.Driver",
      "dbtable" -> "click_statistics",
      "user"->"root",
      "password" -> "Bigdata000000")


    val personRDD = sc.parallelize(Array("1 tom 5", "2 jerry 3", "3 kitty 6")).map(_.split(" "))
    //通过StructType直接指定每个字段的schema
    val schema = StructType(
      List(
        StructField("id", IntegerType, true),
        StructField("name", StringType, true),
        StructField("age", IntegerType, true)
      )
    )
    //将RDD映射到rowRDD
    val rowRDD = personRDD.map(p => Row(p(0).toInt, p(1).trim, p(2).toInt))
    //将schema信息应用到rowRDD上
    val personDataFrame = ssc.createDataFrame(rowRDD, schema)
    //创建Properties存储数据库相关属性
    val prop = new Properties()
    prop.put("user", "root")
    prop.put("password", "123456")
    //将数据追加到数据库
    personDataFrame.write.mode("append").jdbc("jdbc:mysql://192.168.10.1:3306/bigdata", "bigdata.person", prop)
    //停止SparkContext
    sc.stop()
  }
}
