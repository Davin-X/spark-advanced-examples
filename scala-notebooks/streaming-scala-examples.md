# Scala Spark Streaming Examples

> **Note**: This is a "notebook-style" markdown file showing Scala streaming code examples. To run these examples, use SBT or your preferred Scala IDE.

## Setup and Dependencies

Add to your `build.sbt`:

```scala
libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "3.5.0",
  "org.apache.spark" %% "spark-streaming" % "3.5.0",
  "org.apache.spark" %% "spark-sql" % "3.5.0"
)
```

## 1. Basic Socket Streaming (streaming_1.scala)

**Cell 1: Import and Setup**

```scala
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{StreamingContext, Seconds}

// Create Spark configuration
val conf = new SparkConf()
  .setAppName("SocketStreamingExample")
  .setMaster("local[2]") // Need at least 2 threads for streaming

// Create StreamingContext with 5-second batch intervals
val ssc = new StreamingContext(conf, Seconds(5))

println("StreamingContext created with 5-second batches")
```

**Cell 2: Create Socket Stream**

```scala
// Create a DStream from socket connection
// Note: Requires running 'nc -lk 9999' in terminal
val lines = ssc.socketTextStream("localhost", 9999)

println("Socket stream created - will listen on localhost:9999")
println("Start a netcat server with: nc -lk 9999")
println("Then send text data to see streaming in action")
```

**Cell 3: Process the Stream**

```scala
// Split lines into words
val words = lines.flatMap(_.split(" "))

// Count each word
val wordCounts = words.map(word => (word, 1)).reduceByKey(_ + _)

// Print results to console
wordCounts.print()

println("Word count processing pipeline created")
println("Results will be printed every 5 seconds")
```

**Cell 4: Start and Manage Streaming**

```scala
// Start the streaming context
ssc.start()

// Wait for termination (or use Ctrl+C to stop)
// ssc.awaitTermination()

// Graceful shutdown (uncomment in real applications)
// ssc.stop(stopSparkContext = true, stopGracefully = true)

println("Streaming application started!")
println("Send text to netcat server to see word counts")
```

## 2. Advanced Socket Streaming (streaming_2.scala)

**Cell 1: Enhanced Processing**

```scala
import org.apache.spark.streaming.{StreamingContext, Seconds}

// Create streaming context
val ssc = new StreamingContext(sc, Seconds(10))
val lines = ssc.socketTextStream("localhost", 9999)

// Advanced processing pipeline
val processedStream = lines
  .flatMap(line => line.split("\\s+"))     // Split on whitespace
  .filter(word => word.length > 0)         // Remove empty words
  .map(word => word.toLowerCase)           // Convert to lowercase
  .map(word => (word, 1))                  // Create pairs
  .reduceByKey(_ + _)                      // Count words
  .filter { case (word, count) => count > 1 } // Only words appearing > 1 time
  .transform(rdd => rdd.sortBy(_._2, ascending = false)) // Sort by count

processedStream.print()
```

**Cell 2: Windowed Operations**

```scala
// Windowed word counts (30 second window, 10 second slide)
val windowedCounts = words
  .map(word => (word, 1))
  .reduceByKeyAndWindow(
    (a: Int, b: Int) => a + b,        // Add new values
    (a: Int, b: Int) => a - b,        // Remove old values
    Seconds(30),                      // Window duration
    Seconds(10)                       // Slide interval
  )

windowedCounts.print()

println("Windowed operations: 30-second windows, 10-second slides")
```

## 3. Error Handling (streaming_3.scala)

**Cell 1: Robust Stream Processing**

```scala
import org.apache.spark.streaming.{StreamingContext, Seconds}

// Create streaming context with checkpointing
val checkpointDir = "/tmp/spark-checkpoint"
ssc.checkpoint(checkpointDir)

// Error handling wrapper
def safeProcess[T](operation: => T): Option[T] = {
  try {
    Some(operation)
  } catch {
    case e: Exception =>
      println(s"Error in stream processing: ${e.getMessage}")
      None
  }
}
```

**Cell 2: Fault-Tolerant Processing**

```scala
// Socket stream with error handling
val lines = ssc.socketTextStream("localhost", 9999)

// Process with error recovery
val processedWords = lines.transform { rdd =>
  safeProcess {
    rdd.flatMap(_.split(" "))
       .filter(_.nonEmpty)
       .map(word => (word.toLowerCase, 1))
  }.getOrElse(sc.emptyRDD[(String, Int)])
}

// Aggregate with error handling
val wordCounts = processedWords.transform { rdd =>
  safeProcess {
    rdd.reduceByKey(_ + _)
       .sortBy(_._2, ascending = false)
  }.getOrElse(sc.emptyRDD[(String, Int)])
}

wordCounts.print()
```

## 4. Structured Streaming (struct_streaming_*.scala)

**Cell 1: Structured Streaming Setup**

```scala
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import org.apache.spark.sql.streaming.Trigger

// Create Spark session for structured streaming
val spark = SparkSession.builder()
  .appName("StructuredStreamingExample")
  .config("spark.sql.streaming.checkpointLocation", "/tmp/structured-checkpoint")
  .getOrCreate()

import spark.implicits._
```

**Cell 2: File Stream Processing**

```scala
// Read from CSV files in directory
val fileStream = spark.readStream
  .format("csv")
  .option("header", "true")
  .schema("name STRING, age INT, city STRING")
  .load("/path/to/csv/files/")  // Directory to monitor

// Process the stream
val processedStream = fileStream
  .filter($"age" > 18)
  .groupBy($"city")
  .count()
  .orderBy($"count".desc)

// Write to console
val query = processedStream.writeStream
  .outputMode("complete")
  .format("console")
  .trigger(Trigger.ProcessingTime("10 seconds"))
  .start()

// query.awaitTermination()
```

**Cell 3: Windowed Aggregations**

```scala
// Add timestamp for windowing
val streamWithTime = fileStream
  .withColumn("event_time", current_timestamp())
  .withWatermark("event_time", "10 minutes")

// Windowed aggregations
val windowedAgg = streamWithTime
  .groupBy(
    window($"event_time", "5 minutes"),
    $"city"
  )
  .agg(count("*").alias("user_count"))
  .orderBy($"window")

// Output windowed results
val windowQuery = windowedAgg.writeStream
  .outputMode("update")
  .format("console")
  .option("truncate", "false")
  .start()
```

## Running the Examples

### Socket Streaming Examples

1. **Start netcat server:**
   ```bash
   nc -lk 9999
   ```

2. **Run streaming application:**
   ```bash
   sbt "runMain streaming.SocketStreaming"
   ```

3. **Send data to netcat:**
   ```
   hello world hello spark
   apache spark is great
   streaming data processing
   ```

### Structured Streaming Examples

1. **Prepare data directory:**
   ```bash
   mkdir -p /tmp/streaming-data
   echo "name,age,city" > /tmp/streaming-data/users.csv
   echo "Alice,25,NYC" >> /tmp/streaming-data/users.csv
   ```

2. **Run structured streaming:**
   ```bash
   sbt "runMain streaming.StructuredStreaming"
   ```

3. **Add more data files** to see streaming updates

## Key Concepts Demonstrated

### DStream (Basic Streaming)
- **Micro-batch processing**: Fixed time intervals
- **Transformations**: map, flatMap, reduceByKey
- **Output operations**: print, saveAsTextFiles
- **Checkpointing**: Fault tolerance

### Structured Streaming
- **DataFrame API**: Unified batch/streaming
- **Event-time processing**: Handling late data
- **Watermarking**: Managing state
- **Multiple output modes**: append, complete, update

### Error Handling
- **Graceful degradation**: Continue processing on errors
- **Checkpoint recovery**: Restart from last good state
- **Logging**: Monitor and debug streaming applications

## Performance Considerations

### Batch Intervals
- **Small batches (1-5s)**: Low latency, high overhead
- **Large batches (10-30s)**: Better throughput, higher latency
- **Balance**: Based on use case requirements

### Memory Management
- **State management**: Use watermarking to limit state
- **Caching**: Cache intermediate results when appropriate
- **Serialization**: Choose efficient formats

### Fault Tolerance
- **Checkpointing**: Enable for production deployments
- **Idempotent operations**: Design for reprocessing
- **Monitoring**: Track batch processing times

## Comparison with Python Notebooks

| Aspect | Scala Streaming | Python Notebooks |
|--------|----------------|------------------|
| **API Style** | DStream + Structured | Structured Streaming focused |
| **Language** | Scala (static, performant) | Python (dynamic, flexible) |
| **Ecosystem** | JVM ecosystem, Kafka integration | ML/AI ecosystem, Python libraries |
| **Learning Curve** | Steeper for beginners | Gentler for data scientists |
| **Production Use** | Enterprise streaming apps | Research, prototyping, analytics |

## Troubleshooting

### Common Issues

**No data received:**
- Check netcat server is running: `nc -lk 9999`
- Verify correct hostname/port
- Check firewall settings

**Serialization errors:**
- Ensure all custom classes are serializable
- Check object graph doesn't contain non-serializable objects

**Memory issues:**
- Increase executor memory: `--executor-memory 2g`
- Enable Kryo serialization
- Monitor GC and heap usage

---

*These Scala streaming examples demonstrate both DStream and Structured Streaming APIs for comprehensive streaming development.*
