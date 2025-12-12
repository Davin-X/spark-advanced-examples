# Spark Advanced Examples

Production-ready Scala examples for Apache Spark integrations and streaming applications.

## Repository Structure

```
spark-advanced-examples/
├── connectivity/                    # External system integration
│   ├── kafka-producer-consumer/     # Kafka messaging examples
│   ├── hive-integration/           # Hive data warehouse examples
│   └── jdbc-connections/           # Database connectivity
├── streaming/                      # Real-time data processing
│   ├── basic-streaming/            # Socket-based streaming
│   ├── structured-streaming/       # Structured Streaming API
│   └── file-streaming/             # File-based streaming
└── tools/                         # Build and configuration files
```

## Technology Coverage

### Connectivity Examples
- **Apache Kafka**: Producer/consumer patterns with Scala
- **Apache Hive**: Data warehouse integration and table management
- **JDBC Databases**: MySQL connectivity and connection management

### Streaming Applications
- **Socket Streaming**: TCP-based real-time data processing
- **Structured Streaming**: DataFrame-based streaming with SQL operations
- **File Streaming**: Directory monitoring and incremental file processing

## Quick Start

### Prerequisites
- Apache Spark 3.0+
- Scala 2.12+
- Java 8+
- SBT or Maven

### Running Examples

#### Kafka Integration
```bash
# Start Kafka (using Docker)
docker run -d --name kafka -p 9092:9092 spotify/kafka

# Run producer
sbt "runMain HelloProducer"

# Run consumer (separate terminal)
sbt "runMain HelloConsumer"
```

#### Streaming Examples
```bash
# Socket streaming
sbt "runMain streaming.SocketStreaming"

# Structured streaming
sbt "runMain streaming.StructuredStreaming"
```

#### Database Connectivity
```bash
# MySQL connection
sbt "runMain MysqlJDBCConnect"
```

## Learning Objectives

### Enterprise Integration
- Connect Spark with Kafka, Hive, and relational databases
- Implement producer-consumer messaging patterns
- Build reliable data pipelines

### Real-Time Processing
- Process streaming data with Spark Streaming
- Use Structured Streaming for complex transformations
- Handle stateful operations and windowing

### Production Patterns
- Error handling and fault tolerance
- Configuration management
- Performance optimization techniques

## Examples Overview

### Kafka Examples
- **HelloProducer.scala**: Basic message publishing
- **HelloConsumer.scala**: Message consumption and processing
- **HelloProducerTest.scala**: Testing and validation patterns

### Hive Examples
- **FileToHiveTable.scala**: CSV to Hive table conversion
- **connectDockerHive.scala**: Docker-based Hive connectivity

### Streaming Examples
- **Basic Streaming**: Socket-based text processing
- **Structured Streaming**: SQL-like operations on streams
- **File Streaming**: Real-time file system monitoring

## Build Configuration

### SBT (build.sbt)
```scala
name := "spark-advanced-examples"
version := "1.0"
scalaVersion := "2.12.15"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "3.5.0",
  "org.apache.spark" %% "spark-streaming" % "3.5.0",
  "org.apache.spark" %% "spark-sql" % "3.5.0",
  "org.apache.kafka" % "kafka-clients" % "3.4.0"
)
```

### Maven (pom.xml)
Available in `tools/pom.xml` with complete dependency management.

## Documentation Links

- [Spark Streaming Guide](https://spark.apache.org/docs/latest/streaming-programming-guide.html)
- [Structured Streaming](https://spark.apache.org/docs/latest/structured-streaming-programming-guide.html)
- [Kafka Integration](https://spark.apache.org/docs/latest/streaming-kafka-integration.html)

## Contributing

Add new examples following the existing structure:
- Place connectivity examples in `connectivity/`
- Add streaming examples to `streaming/`
- Include build dependencies in `tools/`

---

**Focus: Production-ready Spark integrations and streaming patterns**
