# 🚀 Spark Advanced Examples

[![Apache Spark](https://img.shields.io/badge/Apache%20Spark-3.5.0-red.svg)](https://spark.apache.org/)
[![Scala](https://img.shields.io/badge/Scala-2.12+-orange.svg)](https://scala-lang.org/)

**Advanced Spark Code Examples - Connectivity, Streaming, and Big Data Pipelines!** Production-ready Scala examples for Kafka integration, Hive connectivity, database connections, and real-time streaming applications.

---

## 🎯 **Repository Overview**

This repository consolidates **2 major Spark example repositories**:

### 📚 **Core Components:**
1. **🔗 Connectivity Examples** - Kafka, Hive, and Database Integration
2. **⚡ Streaming Applications** - Real-time Data Processing with Spark Streaming
3. **🏗️ Production Patterns** - Enterprise-scale Spark architectures

---

## 🏗️ **Repository Structure**

```
📦 spark-advanced-examples/                 # Advanced Spark Examples
├── 📖 README.md                             # You're reading it!
├──
├── 🔗 connectivity/                         # External System Integration
│   ├── kafka-producer-consumer/             # Kafka messaging examples
│   │   ├── HelloProducer.scala              # Kafka producer implementation
│   │   ├── HelloConsumer.scala              # Kafka consumer implementation
│   │   └── HelloProducer_test.scala         # Producer testing
│   ├── hive-integration/                    # Hive data warehouse examples
│   │   ├── FileToHiveTable.scala            # CSV to Hive table
│   │   ├── connectDockerHive.scala          # Docker Hive connectivity
│   │   └── metastore_db/                    # Hive metastore configuration
│   └── jdbc-connections/                    # Database connectivity
│       ├── MysqlJDBCConnect.scala           # MySQL JDBC connection
│       └── AppConfigs.scala                 # Configuration management
├──
├── ⚡ streaming/                            # Real-time Data Processing
│   ├── basic-streaming/                     # Socket-based streaming
│   │   ├── streaming_1.scala                # Basic socket streaming
│   │   ├── streaming_2.scala                # Advanced socket processing
│   │   ├── streaming_3.scala                # Error handling and recovery
│   │   └── streaming_4.scala                # Streaming transformations
│   ├── structured-streaming/               # Structured Streaming API
│   │   ├── struct_streaming_1.scala         # Structured streaming basics
│   │   ├── struct_streaming_2.scala         # Complex transformations
│   │   ├── struct_streaming_3.scala         # Windowing operations
│   │   └── struct_streaming_4.scala         # State management
│   └── file-streaming/                      # File-based streaming
│       ├── streaming_file.scala             # File stream processing
│       ├── inputfolder/                     # Sample input files
│       └── outputfolder/                    # Generated output
├──
├── 🛠️ tools/                               # Development utilities
│   ├── build.sbt                            # SBT build configuration
│   ├── pom.xml                             # Maven build configuration
│   └── log4j.properties                    # Logging configuration
├──
├── 📚 resources/                           # Configuration and data
│   ├── AppConfigs.scala                    # Application configurations
│   └── sample-data/                        # Sample input files
├──
└── 🗄️ archive/                            # Original source preservation
    ├── spark-kafka-hive-connctivity-examples/ # Connectivity examples source
    └── Spark-Streaming--Socket/            # Streaming examples source
```

---

## 🎯 **Technology Coverage**

### **🔗 Connectivity Layer**

#### **Apache Kafka Integration**
- **Message Production:** High-throughput data publishing
- **Consumer Patterns:** Real-time data consumption and processing
- **Testing Frameworks:** Producer/consumer validation strategies

#### **Apache Hive Integration**
- **Data Warehousing:** Table creation from files
- **Metastore Management:** Hive catalog and metadata operations
- **Docker Connectivity:** Containerized Hive deployments

#### **Database Connectivity**
- **JDBC Integration:** MySQL and relational databases
- **Connection Management:** Connection pooling and configuration
- **Error Handling:** Robust database failure recovery

### **⚡ Streaming Layer**

#### **Socket-Based Streaming**
- **Network Streaming:** TCP socket data ingestion
- **Processing Patterns:** Real-time data transformations
- **Fault Tolerance:** Stream processing reliability

#### **Structured Streaming**
- **DataFrame Operations:** SQL-like streaming queries
- **Windowing Functions:** Time-based aggregations
- **State Management:** Persistent streaming state

#### **File Streaming**
- **Directory Monitoring:** Real-time file system changes
- **JSON Processing:** Structured data formats
- **Checkpointing:** Stream processing recovery

---

## 🛠️ **Setup & Requirements**

### **Prerequisites**
```bash
# Apache Spark 3.0+
# Scala 2.12+
# Java 8+
# Maven/Gradle or SBT
```

### **External Dependencies**
- **Kafka:** For messaging examples
- **MySQL:** For database connectivity
- **Hive:** For warehouse examples
- **Docker:** For containerized services

### **Running the Examples**

#### **Kafka Examples**
```bash
# Start Kafka cluster
docker run -d --name kafka -p 9092:9092 spotify/kafka

# Run producer example
sbt "runMain org.example.HelloProducer"

# Run consumer in another terminal
sbt "runMain org.example.HelloConsumer"
```

#### **Streaming Examples**
```bash
# Basic socket streaming
sbt "runMain streaming.SocketStreaming"

# Structured streaming
sbt "runMain streaming.StructuredStreaming"

# File streaming
sbt "runMain streaming.FileStreaming"
```

---

## 🎯 **Learning Outcomes**

After exploring these examples, you'll understand:

### **🔗 Enterprise Integration**
- **Big Data Ecosystems:** Connecting Spark with Kafka, Hive, databases
- **Data Pipelines:** Building reliable data flow architectures
- **Scalability Patterns:** Distributed system design principles

### **⚡ Real-Time Processing**
- **Streaming Architectures:** Real-time data processing patterns
- **Fault Tolerance:** Resilient stream processing
- **Performance Tuning:** Optimizing throughput and latency

### **🏭 Production Excellence**
- **Configuration Management:** Enterprise application configs
- **Logging & Monitoring:** Production observability patterns
- **Error Handling:** Robust failure recovery strategies

---

## 📝 **Examples Included**

### **Producer-Consumer Patterns**
- Kafka message publishing with varying throughput
- Consumer group management and offset handling
- Error recovery and dead letter queues

### **Data Warehouse Integration**
- Hive table creation from CSV and JSON files
- Partitioned table management
- Query optimization and performance tuning

### **Streaming Transformations**
- Complex event processing pipelines
- Time-series data aggregation
- Machine learning on streaming data

---

## 🏆 **Professional Value**

This repository demonstrates:

- **🚀 Scalability:** Production-ready distributed systems
- **🔒 Reliability:** Error handling and fault tolerance
- **📊 Performance:** Optimized data processing pipelines
- **🏗️ Architecture:** Enterprise-scale application patterns

---

## 🤝 **Contributions & Learning**

These examples serve as:

- **🚀 Starting Points:** For your Spark projects
- **📚 Learning Resources:** Understanding advanced concepts
- **💼 Portfolio Pieces:** Demonstrating production capabilities
- **🤝 Community Assets:** Shared knowledge and patterns

---

## 📞 **Support & Documentation**

### **Official Documentation**
- [Spark Streaming Programming Guide](https://spark.apache.org/docs/latest/streaming-programming-guide.html)
- [Structured Streaming Documentation](https://spark.apache.org/docs/latest/structured-streaming-programming-guide.html)
- [Spark SQL Documentation](https://spark.apache.org/docs/latest/sql-programming-guide.html)

---

*"Spark is not just a framework - it's a way of thinking about distributed data processing. These examples show you how to build systems that scale beyond imagination."*

**⚡ Happy Spark Engineering! May your pipelines always stream smoothly.** 🏮
