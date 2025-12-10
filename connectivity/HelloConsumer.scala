package org.example

import org.apache.kafka.clients.consumer.{ConsumerConfig, KafkaConsumer}
import org.apache.kafka.common.serialization.{IntegerDeserializer, StringDeserializer}

import java.util.Properties
import scala.collection.JavaConverters._

object HelloConsumer {
  def main(args: Array[String]): Unit = {
    val topicName = AppConfigs.topicName
    println("topic name = " + topicName)
    val numRecords = AppConfigs.numRecords
    println("number of records = " + numRecords)

    val props = new Properties()
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, AppConfigs.bootstrapServers)
    props.put(ConsumerConfig.GROUP_ID_CONFIG, "HelloConsumer")
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, classOf[IntegerDeserializer].getName)
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, classOf[StringDeserializer].getName)

    val consumer = new KafkaConsumer[Integer, String](props)
    consumer.subscribe(Seq(topicName).asJava)

    try {
      var recordsConsumed = 0
      while (recordsConsumed < numRecords) {
        val records = consumer.poll(java.time.Duration.ofMillis(100))
        for (record <- records.asScala) {
          println(s"Received message: " +
            s"key = ${record.key()}, " +
            s"value = ${record.value()}, " +
            s"partition = ${record.partition()}, " +
            s"offset = ${record.offset()}")

          recordsConsumed += 1
          if (recordsConsumed >= numRecords) {
            return
          }
        }
      }
    } finally {
      consumer.close()
    }
  }
}
