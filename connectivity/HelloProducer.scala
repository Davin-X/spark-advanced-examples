package org.example

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}
import org.apache.kafka.common.serialization.{IntegerSerializer, StringSerializer}
import org.apache.logging.log4j.LogManager

import java.util.Properties


object HelloProducer {
  private val logger = LogManager.getLogger

  def main(args: Array[String]): Unit = {
    logger.info("Creating Kafka Producer...")
    val props = new Properties
    props.put(ProducerConfig.CLIENT_ID_CONFIG, AppConfigs.applicationID)
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, AppConfigs.bootstrapServers)
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[IntegerSerializer].getName)
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getName)
    val producer = new KafkaProducer[Integer, String](props)
    logger.info("Start sending messages...")
    var i = 1
    while (i <= AppConfigs.numEvents) {
      producer.send(new ProducerRecord[Integer, String](AppConfigs.topicName, i, "Simple Message-" + i))
      i += 1
    }
    logger.info("Finished - Closing Kafka Producer.")
    producer.close()
  }
}