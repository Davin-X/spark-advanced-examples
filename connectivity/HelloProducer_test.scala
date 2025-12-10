package org.example

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}
import org.apache.kafka.common.KafkaException
import org.apache.kafka.common.serialization.{IntegerSerializer, StringSerializer}
import org.apache.logging.log4j.LogManager

import java.util.Properties


object HelloProducer_test {
  private val logger = LogManager.getLogger(HelloProducer_test)

  def main(args: Array[String]): Unit = {
    var topicName: String = null
    var numEvents = 0
    if (args.length != 2) {
      println("Please provide command line arguments: topicName numEvents")
      System.exit(-1)
    }
    topicName = args(0)
    numEvents = Integer.valueOf(args(1))
    println("Starting HelloProducer...")
    println("topicName=" + topicName + ", numEvents=" + numEvents)
    println("Creating Kafka Producer...")
    logger.info("Starting HelloProducer...")
    logger.debug("topicName=" + topicName + ", numEvents=" + numEvents)
    logger.trace("Creating Kafka Producer...")
    val props = new Properties
    props.put(ProducerConfig.CLIENT_ID_CONFIG, "HelloProducer")
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[IntegerSerializer].getName)
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getName)
    val producer = new KafkaProducer[Integer, String](props)
    logger.trace("Start sending messages...")
    println("Start sending messages...")

    try for (i <- 1 to numEvents) {
      producer.send(new ProducerRecord[Integer, String](topicName, i, "Simple Message-" + i))
    }
    catch {
      case e: KafkaException =>
        println("Exception occurred – Check log for more details.\n" + e.getMessage)
        logger.error("Exception occurred – Check log for more details.\n" + e.getMessage)
        System.exit(-1)
    } finally {
      logger.info("Finished HelloProducer – Closing Kafka Producer.")
      println("Finished HelloProducer – Closing Kafka Producer.")
      producer.close()
    }
  }
}