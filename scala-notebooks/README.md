# Scala Code Notebooks

Interactive-style markdown documentation for Scala Spark examples. These "notebooks" provide formatted code examples that you can copy and run in your Scala environment.

## Overview

Since actual Scala Jupyter notebooks require complex setup (Almond kernel, etc.), these markdown files provide:

- **Formatted code blocks** resembling notebook cells
- **Step-by-step explanations** of Scala concepts
- **Runnable code snippets** ready to copy-paste
- **Educational structure** with clear progression

## Available Notebooks

### [kafka-scala-examples.md](kafka-scala-examples.md)
**Kafka Integration with Scala**
- Direct Kafka client usage (HelloProducer.scala)
- Consumer patterns (HelloConsumer.scala)
- Testing frameworks (HelloProducerTest.scala)
- Low-level producer/consumer APIs

### [streaming-scala-examples.md](streaming-scala-examples.md)
**Spark Streaming with Scala**
- DStream API (socket/file streaming)
- Structured Streaming (DataFrame API)
- Windowing operations and state management
- Error handling and fault tolerance

### [hive-integration-examples.md](hive-integration-examples.md)
**Hive Data Warehouse Integration**
- File to Hive table conversion (FileToHiveTable.scala)
- Docker Hive connectivity (connectDockerHive.scala)
- Partitioned tables and performance optimization
- Data warehousing patterns

### [jdbc-mysql-examples.md](jdbc-mysql-examples.md)
**JDBC/MySQL Database Integration**
- Basic JDBC connections (MysqlJDBCConnect.scala)
- Configuration management (AppConfigs.scala)
- Connection pooling and performance
- Production database integration

### [advanced-streaming-examples.md](advanced-streaming-examples.md)
**Advanced Streaming Patterns**
- Complete coverage of all streaming Scala files
- DStream operations (streaming_1 through streaming_file)
- Structured Streaming (struct_streaming through struct_streaming_4)
- Complex transformations and aggregations
- Fault tolerance and error handling
- Performance optimization techniques

## How to Use

### 1. Choose Your Environment

**SBT Console (Recommended):**
```bash
cd spark-advanced-examples
sbt
```

**Scala IDE:**
- IntelliJ IDEA with Scala plugin
- VS Code with Metals extension
- Eclipse with Scala IDE

### 2. Copy Code Blocks

Each "cell" contains runnable Scala code:
```scala
// Copy this entire block to your Scala environment
val example = "Hello, Spark!"
println(example)
```

### 3. Run and Experiment

- Execute code blocks sequentially
- Modify parameters to see different results
- Experiment with different configurations

## Prerequisites

### For Kafka Examples
```bash
# Start Kafka cluster
docker run -d --name kafka -p 9092:9092 spotify/kafka

# Or install locally
brew install kafka  # macOS
# Follow Kafka documentation for other platforms
```

### For Streaming Examples
```bash
# For socket streaming, start netcat server
nc -lk 9999

# For file streaming, create data directory
mkdir -p /tmp/streaming-input
```

### For Hive Examples
```bash
# Start Hive Docker container
docker run -d --name hive-server -p 10000:10000 -p 10002:10002 \
  --env SERVICE_NAME=hiveserver2 apache/hive:4.0.0
```

### For MySQL Examples
```bash
# Install MySQL and create database
mysql -u root -p -e "CREATE DATABASE testdb;"
mysql -u root -p -e "CREATE USER 'spark'@'localhost' IDENTIFIED BY 'password';"
mysql -u root -p -e "GRANT ALL PRIVILEGES ON testdb.* TO 'spark'@'localhost';"
```

## Learning Approach

### Progressive Learning
1. **Read explanations** in each section
2. **Copy code blocks** to your Scala environment
3. **Execute and observe** results
4. **Modify parameters** to experiment
5. **Combine concepts** from different examples

### Complementary Resources
- **Scala source files**: Actual runnable implementations
- **Python notebooks**: Alternative PySpark approaches
- **Documentation**: Spark official docs for deep dives

## Key Differences from Python Notebooks

| Feature | Scala Notebooks | Python Notebooks |
|---------|----------------|------------------|
| **Execution** | Copy to SBT/IDE | Run in Jupyter |
| **Language** | Scala (JVM) | Python (flexible) |
| **API Level** | Low-level control | High-level abstractions |
| **Performance** | Maximum throughput | Balanced performance |
| **Ecosystem** | Enterprise JVM | Data science Python |

## When to Use Each

### Scala Notebooks (This Directory)
- **Learning low-level APIs**: Direct Kafka clients, DStream operations
- **Production patterns**: Enterprise-grade error handling
- **Performance optimization**: Maximum throughput scenarios
- **JVM ecosystem**: Integration with Java/Scala enterprise tools

### Python Notebooks (notebooks/ Directory)
- **Rapid prototyping**: Quick experimentation
- **Data science workflows**: ML integration, analysis
- **Structured Streaming**: High-level DataFrame operations
- **Learning focus**: Clear explanations and examples

## Example Workflow

### Learning Kafka Integration

1. **Read kafka-scala-examples.md**
2. **Set up Kafka**: `docker run -d --name kafka -p 9092:9092 spotify/kafka`
3. **Copy producer code** to SBT console
4. **Run and observe** message sending
5. **Copy consumer code** and see message receiving
6. **Experiment** with different configurations

### Learning Streaming

1. **Read advanced-streaming-examples.md**
2. **Start netcat**: `nc -lk 9999`
3. **Copy socket streaming code** to SBT
4. **Send test data** to netcat server
5. **Observe word counts** in streaming output
6. **Try windowed operations** for advanced processing

### Learning Database Integration

1. **Read jdbc-mysql-examples.md**
2. **Set up MySQL** database and user
3. **Copy JDBC connection code** to SBT
4. **Run and observe** data reading/writing
5. **Try configuration management** examples

## Troubleshooting

### Common Issues

**Compilation errors:**
- Check `build.sbt` has correct dependencies
- Ensure Scala version compatibility
- Verify import statements

**Runtime errors:**
- Check external services (Kafka, MySQL, Hive) are running
- Verify network connectivity and ports
- Check file permissions for data directories

**Performance issues:**
- Adjust batch intervals for latency vs throughput
- Monitor memory usage with JVM tools
- Enable appropriate logging levels

## Repository Coverage - 100% Complete

These notebooks cover **ALL** Scala files in the repository:

| Category | Scala Files | Notebook Coverage |
|----------|-------------|-------------------|
| **Kafka Integration** | HelloProducer.scala, HelloConsumer.scala, HelloProducerTest.scala | kafka-scala-examples.md ✅ |
| **Hive Integration** | FileToHiveTable.scala, connectDockerHive.scala | hive-integration-examples.md ✅ |
| **JDBC/MySQL** | MysqlJDBCConnect.scala, AppConfigs.scala | jdbc-mysql-examples.md ✅ |
| **Basic Streaming** | streaming_1.scala, streaming_2.scala, streaming_3.scala, streaming_4.scala, streaming_file.scala | advanced-streaming-examples.md ✅ |
| **File Streaming** | streaming_file.scala (in file-streaming/) | advanced-streaming-examples.md ✅ |
| **Structured Streaming** | struct_streaming.scala, struct_streaming_1.scala, struct_streaming_2.scala, struct_streaming_3.scala, struct_streaming_4.scala | advanced-streaming-examples.md ✅ |

**Total Coverage: 18 Scala files across 5 comprehensive notebooks**

---

**🎯 Every Scala file in spark-advanced-examples now has complete notebook documentation!**
