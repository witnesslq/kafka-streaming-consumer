package com.oneapm.spark.kafka

import java.util.Properties
import java.util.concurrent._
import scala.collection.JavaConversions._
import kafka.consumer.Consumer
import kafka.consumer.ConsumerConfig
import kafka.utils._
import kafka.utils.Logging
import kafka.consumer.KafkaStream

class ConsumerExample (zookeeper: String,
                       groupId: String,
                       topic: String,
                       delay: Long) extends Logging {

  val config = createConsumerConfig(zookeeper, groupId)
  val consumer = Consumer.create(config)
  var executor: ExecutorService = null

  def shutdown() = {
    if (consumer != null)
      consumer.shutdown()
    if (executor != null)
      executor.shutdown()
  }

  def createConsumerConfig(zookeeper: String, groupId: String): ConsumerConfig = {
    val props = new Properties()
    props.put("zookeeper.connect", zookeeper)
    props.put("group.id", groupId)
    props.put("auto.offset.reset", "largest")
    props.put("zookeeper.session.timeout.ms", "400")
    props.put("zookeeper.sync.time.ms", "200")
    props.put("auto.commit.interval.ms", "1000")
    val config = new ConsumerConfig(props)
    config 
  }

  def run(numThreads: Int) = {
    val topicCountMap = Map(topic -> numThreads)
    val consumerMap = consumer.createMessageStreams(topicCountMap)
    val streams = consumerMap.get(topic).get

    executor = Executors.newFixedThreadPool(numThreads)
    var threadNum = 0
    for (stream <- streams) {
      executor.submit(new ConsumerTest(stream, threadNum, delay))
      threadNum += 1
    }
  }
}

object ConsumerExample extends App {
  // zookeeper, groupId, topic, delay
  val example = new ConsumerExample(args(0), args(1), args(2), args(3).toLong)
  // threadNum
  example.run(args(4).toInt)
}

class ConsumerTest(val stream: KafkaStream[Array[Byte], Array[Byte]], 
                   val threadNum: Int, 
                   val delay: Long) extends Logging with Runnable {
  def run {
    val it = stream.iterator()

    while (it.hasNext()) {
      val msg = new String(it.next().message())
      System.out.println(msg)
    }
    System.out.println("Shutting down thread:" + threadNum)
  }
}
