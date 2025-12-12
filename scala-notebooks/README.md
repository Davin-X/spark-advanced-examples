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
mkdir -p /tmp/streaming-data
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

1. **Read streaming-scala-examples.md**
2. **Start netcat**: `nc -lk 9999`
3. **Copy socket streaming code** to SBT
4. **Send test data** to netcat server
5. **Observe word counts** in streaming output
6. **Try windowed operations** for advanced processing

## Troubleshooting

### Common Issues

**Compilation errors:**
- Check `build.sbt` has correct dependencies
- Ensure Scala version compatibility
- Verify import statements

**Runtime errors:**
- Check external services (Kafka, netcat) are running
- Verify network connectivity and ports
- Check file permissions for data directories

**Performance issues:**
- Adjust batch intervals for latency vs throughput
- Monitor memory usage with JVM tools
- Enable appropriate logging levels

## Contributing

Add new "notebook" files following this format:
- Use clear section headers
- Format code blocks properly
- Include setup instructions
- Add explanations for complex concepts
- Provide troubleshooting tips

---

**🎯 Start with [kafka-scala-examples.md](kafka-scala-examples.md) to learn low-level Kafka integration in Scala**
