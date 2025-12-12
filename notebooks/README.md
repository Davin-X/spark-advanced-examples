# Interactive Notebooks

Python Jupyter notebooks demonstrating Spark concepts with PySpark. These notebooks provide hands-on learning experiences complementing the Scala code examples.

## Notebooks Overview

### [kafka_integration.ipynb](kafka_integration.ipynb)
**Kafka Integration with PySpark**
- Structured Streaming approach to Kafka
- Producer/consumer patterns using DataFrame API
- Error handling and monitoring
- Comparison with Scala examples

### [streaming_concepts.ipynb](streaming_concepts.ipynb)
**Spark Streaming Concepts**
- DStream vs Structured Streaming
- Socket, file, and structured streaming
- Windowing operations and state management
- Best practices and performance tuning

## Learning Approach

### Interactive Learning
- **Runnable Code**: Execute cells to see immediate results
- **Experimentation**: Modify parameters and observe changes
- **Progressive Complexity**: From basic concepts to advanced patterns
- **Visual Feedback**: DataFrame outputs and streaming results

### Complementary to Scala Examples
- **Scala Code (connectivity/, streaming/)**: Production-ready implementations
- **Python Notebooks (notebooks/)**: Educational exploration and experimentation
- **Cross-Language Understanding**: See concepts implemented in both languages

## Prerequisites

### Environment Setup
```bash
# Install PySpark
pip install pyspark

# For Kafka examples
pip install kafka-python

# For Jupyter notebooks
pip install jupyter
```

### External Services (Optional)
```bash
# Kafka for integration examples
docker run -d --name kafka -p 9092:9092 spotify/kafka

# Netcat for socket streaming
# Terminal: nc -lk 9999
```

## Running the Notebooks

### Start Jupyter
```bash
cd notebooks/
jupyter notebook
# or
jupyter lab
```

### Notebook Workflow
1. **Read explanations** in markdown cells
2. **Execute code cells** sequentially
3. **Modify parameters** to experiment
4. **Observe results** and understand concepts

## Key Differences from Scala Examples

### Scala Examples (Primary Repository)
- **Production-focused**: Optimized for performance
- **Infrastructure-level**: Low-level integration details
- **Enterprise patterns**: Error handling, monitoring, logging

### Python Notebooks (Educational)
- **Learning-focused**: Clear explanations and examples
- **High-level APIs**: DataFrame operations, SQL integration
- **Rapid prototyping**: Quick experimentation and testing

## Use Cases

### When to Use Scala Examples
- Production deployments
- Performance-critical applications
- Custom streaming receivers
- Enterprise integration patterns

### When to Use Python Notebooks
- Learning Spark concepts
- Data analysis and exploration
- Machine learning pipelines
- Prototyping and experimentation

## Contributing

Add new notebooks following these guidelines:
- Include comprehensive explanations
- Provide runnable code examples
- Compare with Scala implementations
- Focus on educational value

---

**🎯 Start with [kafka_integration.ipynb](kafka_integration.ipynb) for external system integration concepts**
