import _root_.kafka.serializer.StringDecoder
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka._

/**
 * Created by root on 15-10-12.
 */
object Test_group {
  def main(args: Array[String]) {
    val zkQuorum:String = "10.4.1.221:2181,10.4.1.222:2181,10.4.1.223:2181"
    val brokers : String = "10.4.1.202:9092,10.4.1.201:9092"
//    val topics : String = "topic_news_behavior,topic_news_social,topic_common_event,topic_scene_behavior"
    val topics : String = "topic_common_event"
    val numRepartition : Int = 2


    val sparkConf = new SparkConf().setAppName("KafkaWordCount").setMaster("local")
    val sc = new SparkContext(sparkConf)
    val data = sc.parallelize(List(1,1,1,4,5,5)).map(item =>(String.valueOf(item),String.valueOf(item)))
    data.collect().foreach(println)
    println(data.groupByKey(3).getNumPartitions)
  }
}
