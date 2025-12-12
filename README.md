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
├── notebooks/                      # Interactive Python tutorials ⭐
│   ├── kafka_integration.ipynb     # PySpark Kafka examples
│   ├── streaming_concepts.ipynb    # Streaming concepts guide
│   └── README.md                   # Notebook usage guide
├── scala-notebooks/               # Scala code documentation 📓
│   ├── kafka-scala-examples.md     # Scala Kafka integration guide
│   ├── streaming-scala-examples.md # Scala streaming guide
│   └── README.md                   # Scala notebook usage
└── tools/                         # Build and configuration files
```

## Technology Coverage

### Connectivity Examples (Scala)
- **Apache Kafka**: Producer/consumer patterns with Scala
- **Apache Hive**: Data warehouse integration and table management
- **JDBC Databases**: MySQL connectivity and connection management

### Streaming Applications (Scala)
- **Socket Streaming**: TCP-based real-time data processing
- **Structured Streaming**: DataFrame-based streaming with SQL operations
- **File Streaming**: Directory monitoring and incremental file processing

### Interactive Notebooks (Python) ⭐
- **Kafka Integration**: PySpark Structured Streaming with Kafka
- **Streaming Concepts**: Comprehensive streaming tutorial
- **Educational Focus**: Hands-on learning with runnable code

### Scala Code Documentation 📓
- **Kafka Examples**: Low-level producer/consumer implementations
- **Streaming Guide**: DStream and Structured Streaming patterns
- **Educational Format**: Notebook-style code documentation

## Quick Start

### Prerequisites
- Apache Spark 3.0+
- Scala 2.12+ (for Scala examples)
- Python 3.8+ (for notebooks)
- SBT or Maven

### Running Scala Examples
```bash
# Kafka producer
sbt "runMain HelloProducer"

# Socket streaming
sbt "runMain streaming.SocketStreaming"
```

### Running Python Notebooks ⭐
```bash
# Install Jupyter and PySpark
pip install jupyter pyspark

# Start notebooks
cd notebooks/
jupyter notebook

# Open kafka_integration.ipynb or streaming_concepts.ipynb
```

### Learning Scala Code 📓
```bash
# Read formatted documentation
cd scala-notebooks/
cat kafka-scala-examples.md  # Copy code to SBT console
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

### Interactive Learning ⭐
- Experiment with PySpark in Jupyter notebooks
- Understand concepts through runnable examples
- Compare Scala and Python implementations

### Scala Code Documentation 📓
- Learn low-level Scala APIs for Spark
- Understand direct Kafka client usage
- Master DStream and Structured Streaming patterns

## Content Types

### 📄 Scala Source Files
Production-ready implementations for enterprise use cases.

### 📓 Python Notebooks ⭐
Interactive educational content with runnable examples.

### 📝 Scala Code Notebooks 📓
Formatted documentation with copy-paste Scala code examples.

### 🛠️ Build Configuration
SBT, Maven, and logging configurations.

## Examples Overview

### Kafka Examples
- **HelloProducer.scala**: Basic message publishing (Scala)
- **HelloConsumer.scala**: Message consumption and processing (Scala)
- **[kafka_integration.ipynb](notebooks/kafka_integration.ipynb)**: PySpark Structured Streaming (Python) ⭐
- **[kafka-scala-examples.md](scala-notebooks/kafka-scala-examples.md)**: Scala Kafka client guide 📓

### Streaming Examples
- **Basic Streaming**: Socket-based text processing (Scala)
- **Structured Streaming**: SQL-like operations on streams (Scala)
- **[streaming_concepts.ipynb](notebooks/streaming_concepts.ipynb)**: Comprehensive streaming guide (Python) ⭐
- **[streaming-scala-examples.md](scala-notebooks/streaming-scala-examples.md)**: Scala streaming patterns 📓

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

### Python (requirements.txt)
```
pyspark>=3.5.0
jupyter>=1.0.0
kafka-python>=2.0.0
```

## Documentation Links

- [Spark Streaming Guide](https://spark.apache.org/docs/latest/streaming-programming-guide.html)
- [Structured Streaming](https://spark.apache.org/docs/latest/structured-streaming-programming-guide.html)
- [Kafka Integration](https://spark.apache.org/docs/latest/streaming-kafka-integration.html)

## Learning Paths

### Path 1: Python-First Learning ⭐
```
notebooks/ → Interactive experimentation
- Jupyter notebooks with runnable examples
- Clear explanations and visualizations
- Perfect for learning Spark concepts
```

### Path 2: Scala Production Development 📓
```
scala-notebooks/ + Scala files → Enterprise development
- Low-level Scala APIs for maximum performance
- Production-ready error handling patterns
- JVM ecosystem integration
```

### Path 3: Complete Understanding 🔄
```
All resources → Full-stack Spark mastery
- Compare Python vs Scala approaches
- Choose right tool for each use case
- Build comprehensive Spark solutions
```

## Contributing

### Scala Examples
Add new examples in the appropriate `connectivity/` or `streaming/` subdirectories.

### Python Notebooks ⭐
Add educational notebooks to `notebooks/` that complement Scala examples.

### Scala Code Documentation 📓
Add formatted "notebook" documentation to `scala-notebooks/` for complex Scala examples.

---

**🚀 For hands-on learning: Start with [notebooks/](notebooks/)**
**🏭 For production code: Use Scala examples in [connectivity/](connectivity/) and [streaming/](streaming/)**
**📓 For Scala guidance: Check [scala-notebooks/](scala-notebooks/)**
