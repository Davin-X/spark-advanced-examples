# Scala Advanced Streaming Examples

> **Note**: This is a "notebook-style" markdown file showing advanced Scala streaming code examples. To run these examples, use SBT or your preferred Scala IDE.

## Setup and Dependencies

Add to your `build.sbt`:

```scala
libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "3.5.0",
  "org.apache.spark" %% "spark-streaming" % "3.5.0",
  "org.apache.spark" %% "spark-sql" % "3.5.0"
)
```

## 1. streaming_1.scala - Basic Socket Word Count

**Cell 1: Basic Streaming Setup**

```scala
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{StreamingContext, Seconds}

// Create streaming context
val conf = new SparkConf()
  .setAppName("SocketWordCount")
  .setMaster("local[2]")

val ssc = new StreamingContext(conf, Seconds(5))

println("StreamingContext created with 5-second batch interval")
println("Requires netcat server: nc -lk 9999")
```

**Cell 2: Socket Stream Processing**

```scala
// Create socket stream on port 9999
val lines = ssc.socketTextStream("localhost", 9999)

// Process the stream
val words = lines.flatMap(_.split(" "))
val wordCounts = words.map(word => (word, 1)).reduceByKey(_ + _)

// Print results
wordCounts.print()

println("Word count processing pipeline created")
println("Send text to netcat server to see results")
```

**Cell 3: Start and Monitor Streaming**

```scala
// Start the streaming context
ssc.start()

// Wait for termination
// ssc.awaitTermination()

println("Streaming application started!")
println("Monitor the console for word count results")
println("Send text data to netcat server to generate streaming output")
```

## 2. streaming_2.scala - Enhanced Processing

**Cell 1: Advanced Transformations**

```scala
import org.apache.spark.streaming.{StreamingContext, Seconds}

val ssc = new StreamingContext(sc, Seconds(10))
val lines = ssc.socketTextStream("localhost", 9999)

// Enhanced processing pipeline
val processedWords = lines
  .flatMap(line => line.split("\\s+"))        // Split on whitespace
  .filter(word => word.length > 0)            // Remove empty words
  .map(word => word.toLowerCase)              // Convert to lowercase
  .filter(word => !word.matches(".*\\d.*"))   // Remove words with numbers
  .map(word => (word, 1))                     // Create pairs
  .reduceByKey(_ + _)                         // Count words
  .filter { case (word, count) => count > 2 } // Only frequent words
  .transform(rdd => rdd.sortBy(_._2, ascending = false)) // Sort by count

processedWords.print()
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

// Filter and sort windowed results
val topWords = windowedCounts
  .filter { case (word, count) => count > 5 }
  .transform(rdd => rdd.sortBy(_._2, ascending = false))
  .transform(rdd => sc.parallelize(rdd.take(10))) // Top 10

topWords.print()
```

## 3. streaming_3.scala - Error Handling

**Cell 1: Fault-Tolerant Streaming**

```scala
import org.apache.spark.streaming.{StreamingContext, Seconds}

// Create streaming context with checkpointing
val ssc = new StreamingContext(sc, Seconds(5))
val checkpointDir = "/tmp/spark-streaming-checkpoint"

// Enable checkpointing for fault tolerance
ssc.checkpoint(checkpointDir)

// Error handling wrapper
def safeProcess[T](operation: => T): Option[T] = {
  try {
    Some(operation)
  } catch {
    case e: Exception =>
      println(s"Error in stream processing: ${e.getMessage}")
      e.printStackTrace()
      None
  }
}
```

**Cell 2: Robust Stream Processing**

```scala
// Create socket stream with error handling
val lines = ssc.socketTextStream("localhost", 9999)

// Process with error recovery
val processedStream = lines.transform { rdd =>
  safeProcess {
    rdd.flatMap(_.split(" "))
       .filter(_.nonEmpty)
       .map(word => (word.toLowerCase, 1))
  }.getOrElse(sc.emptyRDD[(String, Int)])
}

// Aggregate with error handling
val wordCounts = processedStream.transform { rdd =>
  safeProcess {
    rdd.reduceByKey(_ + _)
       .sortBy(_._2, ascending = false)
       .filter { case (word, count) => count > 1 }
  }.getOrElse(sc.emptyRDD[(String, Int)])
}

wordCounts.print()
```

## 4. streaming_4.scala - Advanced Operations

**Cell 1: Complex Stream Processing**

```scala
import org.apache.spark.streaming.{StreamingContext, Seconds}

val ssc = new StreamingContext(sc, Seconds(5))
val lines = ssc.socketTextStream("localhost", 9999)

// Multiple parallel processing branches
val words = lines.flatMap(_.split(" "))

// Branch 1: Word count
val wordCountStream = words
  .map(word => (word, 1))
  .reduceByKey(_ + _)

// Branch 2: Word length analysis
val wordLengthStream = words
  .map(word => (word.length, 1))
  .reduceByKey(_ + _)

// Branch 3: Character count
val charCountStream = words
  .map(word => word.length)
  .reduce(_ + _)
  .map(total => ("total_chars", total))
```

**Cell 2: Union and Join Operations**

```scala
// Combine multiple streams
val combinedStream = wordCountStream.union(wordLengthStream)

// Join with state (using updateStateByKey)
def updateFunction(newValues: Seq[Int], runningCount: Option[Int]): Option[Int] = {
  val newCount = newValues.sum
  val totalCount = runningCount.getOrElse(0) + newCount
  Some(totalCount)
}

val runningCounts = wordCountStream.updateStateByKey(updateFunction)
runningCounts.print()
```

## 5. streaming_file.scala - File Streaming

**Cell 1: File Stream Setup**

```scala
import org.apache.spark.streaming.{StreamingContext, Seconds}
import org.apache.spark.streaming.dstream.DStream

val ssc = new StreamingContext(sc, Seconds(10))

// Monitor directory for new files
val fileStream = ssc.textFileStream("/tmp/streaming-input")

println("File stream created - monitoring /tmp/streaming-input directory")
println("Add text files to see streaming processing")
```

**Cell 2: File Processing Pipeline**

```scala
// Process file stream
val processedFiles = fileStream
  .flatMap(line => line.split(" "))
  .filter(word => word.length > 3)  // Only words longer than 3 chars
  .map(word => (word.toLowerCase, 1))
  .reduceByKey(_ + _)
  .filter { case (word, count) => count > 1 }  // Words appearing > 1 time
  .transform(rdd => rdd.sortBy(_._2, ascending = false))

processedFiles.print()

// Save results
processedFiles.saveAsTextFiles("/tmp/streaming-output/wordcount")
```

## Structured Streaming Examples

## 6. struct_streaming_1.scala - Basic Structured Streaming

**Cell 1: Rate Stream Example**

```scala
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

val spark = SparkSession.builder()
  .appName("StructuredStreamingBasic")
  .getOrCreate()

// Create rate stream (generates data automatically)
val rateStream = spark.readStream
  .format("rate")
  .option("rowsPerSecond", "5")
  .option("numPartitions", "2")
  .load()

// Process the stream
val processedStream = rateStream
  .withColumn("timestamp", current_timestamp())
  .withColumn("batch_id", spark_partition_id())

// Output to console
val query = processedStream.writeStream
  .outputMode("append")
  .format("console")
  .option("truncate", "false")
  .start()

// query.awaitTermination()
```

## 7. struct_streaming_2.scala - Complex Transformations

**Cell 1: Advanced Processing**

```scala
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

val spark = SparkSession.builder()
  .appName("StructuredStreamingAdvanced")
  .getOrCreate()

// Rate stream with complex processing
val rateStream = spark.readStream
  .format("rate")
  .option("rowsPerSecond", "10")
  .load()

// Complex transformations
val complexStream = rateStream
  .withColumn("event_time", current_timestamp())
  .withColumn("category", 
    when(col("value") % 3 === 0, "divisible_by_3")
    .when(col("value") % 2 === 0, "even")
    .otherwise("odd"))
  .withColumn("processed_at", current_timestamp())
  .groupBy(
    window(col("event_time"), "30 seconds"),
    col("category")
  )
  .agg(
    count("*").alias("count"),
    avg("value").alias("avg_value"),
    min("value").alias("min_value"),
    max("value").alias("max_value")
  )
  .orderBy("window", "category")

// Output results
val query = complexStream.writeStream
  .outputMode("complete")
  .format("console")
  .option("truncate", "false")
  .start()
```

## Running the Examples

### DStream Examples

1. **Start netcat server:**
   ```bash
   nc -lk 9999
   ```

2. **Run streaming examples:**
   ```bash
   sbt "runMain streaming.SocketStreaming"
   sbt "runMain streaming.SocketStreamingAdvanced"
   sbt "runMain streaming.SocketStreamingErrorHandling"
   ```

3. **Send test data:**
   ```
   hello world this is a test message
   apache spark streaming example
   big data processing with scala
   ```

### Structured Streaming Examples

1. **Run structured streaming:**
   ```bash
   sbt "runMain streaming.StructuredStreaming"
   ```

2. **Observe automatic data generation and processing**

### File Streaming Example

1. **Create input directory:**
   ```bash
   mkdir -p /tmp/streaming-input
   ```

2. **Add text files:**
   ```bash
   echo "spark hadoop kafka streaming" > /tmp/streaming-input/file1.txt
   echo "machine learning data science" > /tmp/streaming-input/file2.txt
   ```

3. **Run file streaming:**
   ```bash
   sbt "runMain streaming.FileStreaming"
   ```

## Key Concepts Demonstrated

### DStream Operations
- **Transformations**: map, flatMap, filter, reduceByKey
- **Windowing**: reduceByKeyAndWindow, sliding windows
- **State Management**: updateStateByKey for stateful processing
- **Fault Tolerance**: Checkpointing and error recovery

### Structured Streaming
- **DataFrame API**: Unified batch/streaming processing
- **Window Functions**: Time-based aggregations
- **Watermarking**: Handling late-arriving data
- **Output Modes**: append, complete, update

### Error Handling
- **Graceful Degradation**: Continue processing on errors
- **Logging**: Comprehensive error reporting
- **Recovery**: Checkpoint-based fault tolerance

## Performance Considerations

### Batch Intervals
```scala
// Smaller batches for low latency
val ssc = new StreamingContext(sc, Seconds(1))  // Fast processing

// Larger batches for high throughput
val ssc = new StreamingContext(sc, Seconds(30)) // Efficient processing
```

### Parallelism
```scala
// Increase receiver parallelism
ssc.sparkContext.set("spark.streaming.concurrentJobs", "4")

// Configure block interval
ssc.sparkContext.set("spark.streaming.blockInterval", "200ms")
```

### Memory Management
```scala
// Enable Kryo serialization
conf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")

// Configure executor memory
conf.set("spark.executor.memory", "2g")
```

## Troubleshooting

### Common DStream Issues

**No data received:**
- Check netcat server: `ps aux | grep nc`
- Verify port accessibility: `telnet localhost 9999`
- Check firewall settings

**Serialization errors:**
- Ensure custom classes are serializable
- Use Kryo for complex objects
- Avoid non-serializable closures

**Memory issues:**
- Reduce batch interval
- Increase executor memory
- Enable backpressure

### Common Structured Streaming Issues

**Watermark errors:**
- Set appropriate watermark delay
- Ensure event time column exists
- Check timestamp format

**Output mode conflicts:**
- Use "append" for append-only operations
- Use "complete" for aggregations
- Use "update" for selective updates

**Checkpoint issues:**
- Ensure checkpoint directory is accessible
- Clear checkpoint for schema changes
- Monitor checkpoint size

---

*These advanced Scala streaming examples demonstrate both DStream and Structured Streaming APIs for complex real-time data processing scenarios.*

## 8. struct_streaming_3.scala - Windowing Operations

**Cell 1: Time-Based Windowing**

```scala
import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.streaming.Trigger

object struct_streaming_3 extends App {
  Logger.getLogger("org").setLevel(Level.ERROR)

  val spark = SparkSession.builder()
    .master("local[2]")
    .appName("streaming_windowing")
    .getOrCreate()

  import spark.implicits._

  // Create rate stream for windowing examples
  val rateStream = spark.readStream
    .format("rate")
    .option("rowsPerSecond", "5")
    .load()
    .withColumn("timestamp", $"timestamp".cast("timestamp"))

  println("Windowing operations with Structured Streaming")
```

**Cell 2: Tumbling Windows**

```scala
  // Tumbling window - non-overlapping time windows
  val tumblingWindow = rateStream
    .groupBy(
      window($"timestamp", "10 minutes")  // 10-minute windows
    )
    .count()
    .orderBy("window")

  // Output tumbling windows
  val tumblingQuery = tumblingWindow.writeStream
    .outputMode("complete")
    .format("console")
    .option("truncate", "false")
    .trigger(Trigger.ProcessingTime("30 seconds"))
    .start()

  // tumblingQuery.awaitTermination()
  println("Tumbling window example - 10-minute non-overlapping windows")
```

## 9. struct_streaming_4.scala - Advanced Windowing and Joins

**Cell 1: Sliding Windows and Watermarking**

```scala
import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.{SparkSession, functions}
import org.apache.spark.sql.functions.{col, sum, window}
import org.apache.spark.sql.streaming.Trigger

object struct_streaming_4 extends App {
  Logger.getLogger("org").setLevel(Level.ERROR)

  val spark = SparkSession.builder()
    .master("local[2]")
    .appName("advanced_windowing")
    .getOrCreate()

  import spark.implicits._
```

**Cell 2: Sliding Windows with Watermark**

```scala
  // Create stream with sliding windows
  val slidingStream = spark.readStream
    .format("rate")
    .option("rowsPerSecond", "10")
    .load()
    .withColumn("timestamp", $"timestamp".cast("timestamp"))
    .withColumn("event_type", 
      when($"value" % 3 === 0, "type_A")
      .when($"value" % 2 === 0, "type_B")
      .otherwise("type_C"))

  // Sliding windows: 15-minute window, 5-minute slide
  val slidingWindow = slidingStream
    .withWatermark("timestamp", "10 minutes")  // Handle late data
    .groupBy(
      window($"timestamp", "15 minutes", "5 minutes"),  // Sliding window
      $"event_type"
    )
    .agg(sum("value").alias("total_value"))
    .orderBy("window", "event_type")

  // Output sliding window results
  val slidingQuery = slidingWindow.writeStream
    .outputMode("update")
    .format("console")
    .option("truncate", "false")
    .start()

  // slidingQuery.awaitTermination()
  println("Sliding window with watermarking - handles late-arriving data")
```

## 10. struct_streaming.scala - Basic Structured Streaming

**Cell 1: Fundamental Structured Streaming**

```scala
import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

object struct_streaming extends App {
  Logger.getLogger("org").setLevel(Level.ERROR)

  val spark = SparkSession.builder()
    .master("local[2]")
    .appName("basic_structured_streaming")
    .getOrCreate()

  import spark.implicits._
```

**Cell 2: Simple Rate Stream**

```scala
  // Basic rate stream - generates data automatically
  val rateStream = spark.readStream
    .format("rate")
    .option("rowsPerSecond", "3")  // Generate 3 rows per second
    .option("numPartitions", "1")  // Single partition for simplicity
    .load()

  // Add processing timestamp
  val processedStream = rateStream
    .withColumn("processing_time", functions.current_timestamp())
    .withColumn("batch_id", functions.spark_partition_id())

  // Display stream
  val query = processedStream.writeStream
    .outputMode("append")
    .format("console")
    .option("truncate", "false")
    .start()

  // query.awaitTermination()
  println("Basic structured streaming - automatic data generation")
```

**Cell 3: Stream Processing Patterns**

```scala
  // Filter and transform
  val filteredStream = rateStream
    .filter($"value" > 10)  // Only values > 10
    .withColumn("category",
      when($"value" % 2 === 0, "even")
      .otherwise("odd"))
    .groupBy("category")
    .count()

  // Output aggregated results
  val aggQuery = filteredStream.writeStream
    .outputMode("complete")
    .format("console")
    .trigger(Trigger.ProcessingTime("10 seconds"))
    .start()

  // aggQuery.awaitTermination()
  println("Stream filtering and aggregation patterns")
```

## Running Structured Streaming Examples

### struct_streaming_3 (Windowing)

1. **Run windowing example:**
   ```bash
   sbt "runMain streaming.struct_streaming_3"
   ```

2. **Observe tumbling windows:**
   - 10-minute non-overlapping windows
   - Count aggregation per window
   - Real-time window progression

### struct_streaming_4 (Advanced Windowing)

1. **Run advanced windowing:**
   ```bash
   sbt "runMain streaming.struct_streaming_4"
   ```

2. **Observe sliding windows:**
   - 15-minute windows with 5-minute slides
   - Watermarking for late data
   - Multiple event types

### struct_streaming (Basic)

1. **Run basic structured streaming:**
   ```bash
   sbt "runMain streaming.struct_streaming"
   ```

2. **Observe automatic data:**
   - Rate-based data generation
   - Real-time processing timestamps
   - Basic filtering and aggregation

## Complete Structured Streaming Coverage

| Example | Focus | Key Features |
|---------|-------|--------------|
| struct_streaming | Basics | Rate streams, basic processing |
| struct_streaming_1 | Rate Processing | Automatic data generation |
| struct_streaming_2 | Complex Transforms | Advanced aggregations |
| struct_streaming_3 | Windowing | Tumbling windows |
| struct_streaming_4 | Advanced Windowing | Sliding windows, watermarking |

## Structured Streaming Key Concepts

### Windowing Types
- **Tumbling Windows**: Non-overlapping, fixed-size time windows
- **Sliding Windows**: Overlapping windows with configurable slide intervals
- **Session Windows**: Dynamic windows based on activity gaps

### Watermarking
- **Late Data Handling**: Accept data arriving after window closes
- **State Management**: Control state size and memory usage
- **Completeness**: Ensure accurate results with delayed data

### Output Modes
- **Append**: Only new rows added to result table
- **Complete**: Entire result table rewritten each time
- **Update**: Only changed rows in result table

---

*These structured streaming examples demonstrate the full spectrum of time-based processing, windowing operations, and state management in Apache Spark.*
