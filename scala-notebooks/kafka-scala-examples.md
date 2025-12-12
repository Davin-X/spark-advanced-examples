# Scala Kafka Integration Examples

> **Note**: This is a "notebook-style" markdown file showing Scala code examples. To run these examples, use your preferred Scala IDE or SBT console.

## Setup and Dependencies

First, ensure you have the required dependencies in your `build.sbt`:

```scala
libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "3.5.0",
  "org.apache.spark" %% "spark-streaming" % "3.5.0",
  "org.apache.spark" %% "spark-streaming-kafka-0-10" % "3.5.0",
  "org.apache.kafka" % "kafka-clients" % "3.4.0"
)
```

## 1. HelloProducer.scala - Kafka Message Producer

**Cell 1: Imports and Setup**

```scala
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import java.util.Properties
import scala.collection.JavaConverters._

// Kafka producer configuration
val props = new Properties()
props.put("bootstrap.servers", "localhost:9092")
props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

// Create producer
val producer = new KafkaProducer[String, String](props)
```

**Cell 2: Send Messages**

```scala
// Sample data to send
val messages = List(
  ("user-1", """{"name": "Alice", "age": 25, "city": "New York"}"""),
  ("user-2", """{"name": "Bob", "age": 30, "city": "San Francisco"}"""),
  ("user-3", """{"name": "Charlie", "age": 35, "city": "Chicago"}""")
)

// Send messages to Kafka topic
messages.foreach { case (key, value) =>
  val record = new ProducerRecord[String, String]("user-events", key, value)
  producer.send(record)
  println(s"Sent message: key=$key, value=$value")
}

// Close producer
producer.close()
```

## 2. HelloConsumer.scala - Kafka Message Consumer

**Cell 1: Consumer Setup**

```scala
import org.apache.kafka.clients.consumer.{KafkaConsumer, ConsumerRecords, ConsumerRecord}
import java.util.Properties
import scala.collection.JavaConverters._

// Consumer configuration
val props = new Properties()
props.put("bootstrap.servers", "localhost:9092")
props.put("group.id", "scala-consumer-group")
props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
props.put("auto.offset.reset", "earliest")

// Create consumer
val consumer = new KafkaConsumer[String, String](props)

// Subscribe to topic
consumer.subscribe(List("user-events").asJava)
```

**Cell 2: Consume Messages**

```scala
// Poll for messages
println("Listening for messages...")

while (true) {
  val records: ConsumerRecords[String, String] = consumer.poll(java.time.Duration.ofMillis(100))
  
  records.asScala.foreach { record: ConsumerRecord[String, String] =>
    println(s"Received: key=${record.key()}, value=${record.value()}, partition=${record.partition()}, offset=${record.offset()}")
  }
  
  Thread.sleep(1000) // Poll every second
}

// In a real application, handle shutdown gracefully
// consumer.close()
```

## 3. HelloProducerTest.scala - Testing Producer

**Cell 1: Test Setup**

```scala
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord, Callback, RecordMetadata}
import java.util.Properties

// Test configuration
val testProps = new Properties()
testProps.put("bootstrap.servers", "localhost:9092")
testProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
testProps.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
testProps.put("acks", "all") // Wait for all replicas
testProps.put("retries", "3") // Retry on failure

val testProducer = new KafkaProducer[String, String](testProps)
```

**Cell 2: Test Message Production with Callbacks**

```scala
// Test data
val testMessages = List(
  ("test-1", "Test message 1"),
  ("test-2", "Test message 2"),
  ("test-3", "Test message 3")
)

// Custom callback for delivery confirmation
class TestCallback extends Callback {
  override def onCompletion(metadata: RecordMetadata, exception: Exception): Unit = {
    if (exception == null) {
      println(s"Message delivered successfully: partition=${metadata.partition()}, offset=${metadata.offset()}")
    } else {
      println(s"Message delivery failed: ${exception.getMessage}")
    }
  }
}

// Send test messages
testMessages.foreach { case (key, value) =>
  val record = new ProducerRecord[String, String]("test-topic", key, value)
  testProducer.send(record, new TestCallback())
}

// Flush to ensure all messages are sent
testProducer.flush()
testProducer.close()

println("Test completed - check consumer output for delivery confirmations")
```

## Running the Examples

### Using SBT Console

```bash
# Start SBT
sbt

# Run individual examples
sbt "runMain HelloProducer"
sbt "runMain HelloConsumer"
sbt "runMain HelloProducerTest"
```

### Using Scala IDE (IntelliJ IDEA, VS Code)

1. Open the project in your IDE
2. Ensure Kafka is running: `docker run -d --name kafka -p 9092:9092 spotify/kafka`
3. Run each main class individually
4. Monitor output in console/logs

### Expected Output

**Producer Output:**
```
Sent message: key=user-1, value={"name": "Alice", "age": 25, "city": "New York"}
Sent message: key=user-2, value={"name": "Bob", "age": 30, "city": "San Francisco"}
Sent message: key=user-3, value={"name": "Charlie", "age": 35, "city": "Chicago"}
```

**Consumer Output:**
```
Received: key=user-1, value={"name": "Alice", "age": 25, "city": "New York"}, partition=0, offset=0
Received: key=user-2, value={"name": "Bob", "age": 30, "city": "San Francisco"}, partition=0, offset=1
Received: key=user-3, value={"name": "Charlie", "age": 35, "city": "Chicago"}, partition=0, offset=2
```

## Key Concepts Covered

### Producer Concepts
- **Serialization**: Converting Scala objects to bytes
- **Partitioning**: How messages are distributed
- **Delivery Guarantees**: acks, retries, callbacks
- **Error Handling**: Connection failures, broker unavailability

### Consumer Concepts
- **Consumer Groups**: Load balancing and fault tolerance
- **Offset Management**: Tracking message processing
- **Polling**: Efficient message retrieval
- **Deserialization**: Converting bytes back to Scala objects

### Testing Concepts
- **Reliability Testing**: Ensuring message delivery
- **Performance Monitoring**: Throughput and latency
- **Failure Scenarios**: Network issues, broker failures

## Comparison with Python Notebooks

| Feature | Scala Examples | Python Notebooks |
|---------|----------------|------------------|
| **API Level** | Low-level Kafka client | High-level Structured Streaming |
| **Language** | Scala (JVM) | Python (flexible) |
| **Use Case** | Custom producers/consumers | Data processing pipelines |
| **Performance** | Maximum throughput | Balanced performance |
| **Complexity** | More control, more code | Simpler, declarative |

## Next Steps

1. **Run the examples** with a local Kafka cluster
2. **Experiment** with different configurations
3. **Explore advanced features** like custom partitioning
4. **Check the Python notebooks** for Structured Streaming approach
5. **Build production pipelines** combining both approaches

---

*These "notebook-style" examples show Scala Kafka integration code that you can copy and run in your Scala environment.*
