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
