# Scala Hive Integration Examples

> **Note**: This is a "notebook-style" markdown file showing Scala Hive integration code examples. To run these examples, use SBT or your preferred Scala IDE.

## Setup and Dependencies

Add to your `build.sbt`:

```scala
libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "3.5.0",
  "org.apache.spark" %% "spark-hive" % "3.5.0",
  "org.apache.spark" %% "spark-sql" % "3.5.0"
)
```

## 1. FileToHiveTable.scala - CSV to Hive Table

**Cell 1: Spark Session Setup with Hive Support**

```scala
import org.apache.spark.sql.SparkSession

// Create Spark session with Hive support
val spark = SparkSession.builder()
  .appName("FileToHiveTable")
  .config("spark.sql.warehouse.dir", "/user/hive/warehouse")  // Hive warehouse location
  .enableHiveSupport()
  .getOrCreate()

// Set legacy time parser policy for compatibility
spark.conf.set("spark.sql.legacy.timeParserPolicy", "LEGACY")

import spark.implicits._
import spark.sql
```

**Cell 2: Create Sample Data**

```scala
// Create sample employee data
val employeeData = Seq(
  (1, "John Doe", "Engineering", 75000.0, "2020-01-15"),
  (2, "Jane Smith", "Marketing", 65000.0, "2020-02-20"),
  (3, "Bob Johnson", "Engineering", 80000.0, "2020-03-10"),
  (4, "Alice Brown", "HR", 60000.0, "2020-04-05"),
  (5, "Charlie Wilson", "Engineering", 85000.0, "2020-05-12")
)

// Convert to DataFrame
val df = employeeData.toDF("id", "name", "department", "salary", "hire_date")
df.show()
```

**Cell 3: Create Hive Database and Table**

```scala
// Create database if it doesn't exist
spark.sql("CREATE DATABASE IF NOT EXISTS employee_db")

// Use the database
spark.sql("USE employee_db")

// Create Hive table from DataFrame
df.write
  .mode("overwrite")
  .saveAsTable("employees")

println("Hive table 'employees' created successfully")
```

**Cell 4: Query the Hive Table**

```scala
// Query the table
val result = spark.sql("""
  SELECT department, 
         COUNT(*) as employee_count, 
         AVG(salary) as avg_salary,
         MAX(salary) as max_salary
  FROM employees 
  GROUP BY department 
  ORDER BY avg_salary DESC
""")

result.show()

// Show table structure
spark.sql("DESCRIBE employees").show()
```

## 2. connectDockerHive.scala - Docker Hive Connectivity

**Cell 1: Docker Environment Setup**

```scala
import org.apache.spark.sql.SparkSession
import java.util.Properties

// Docker Hive configuration
val hiveHost = "localhost"  // Docker container hostname
val hivePort = "10000"      // HiveServer2 port
val hiveDatabase = "default"

// JDBC URL for Hive
val hiveUrl = s"jdbc:hive2://$hiveHost:$hivePort/$hiveDatabase"

// Connection properties
val connectionProperties = new Properties()
connectionProperties.put("user", "hive")
connectionProperties.put("password", "")
connectionProperties.put("driver", "org.apache.hive.jdbc.HiveDriver")
```

**Cell 2: Connect to Hive via JDBC**

```scala
// Create Spark session
val spark = SparkSession.builder()
  .appName("DockerHiveConnection")
  .config("spark.sql.warehouse.dir", "/user/hive/warehouse")
  .enableHiveSupport()
  .getOrCreate()

// Test connection by listing databases
try {
  val databasesDF = spark.sql("SHOW DATABASES")
  databasesDF.show()
  
  println("Successfully connected to Hive via Docker!")
} catch {
  case e: Exception => 
    println(s"Connection failed: ${e.getMessage}")
    println("Make sure Hive Docker container is running on port 10000")
}
```

**Cell 3: Create and Populate Tables**

```scala
// Create a test table
spark.sql("""
  CREATE TABLE IF NOT EXISTS test_table (
    id INT,
    name STRING,
    category STRING,
    value DOUBLE
  )
  STORED AS PARQUET
""")

// Insert sample data
spark.sql("""
  INSERT INTO test_table VALUES
  (1, 'Product A', 'Electronics', 299.99),
  (2, 'Product B', 'Books', 19.99),
  (3, 'Product C', 'Clothing', 49.99)
""")

// Query the data
val results = spark.sql("SELECT * FROM test_table ORDER BY value DESC")
results.show()
```

**Cell 4: Advanced Hive Operations**

```scala
// Create partitioned table
spark.sql("""
  CREATE TABLE IF NOT EXISTS sales_partitioned (
    product_id INT,
    sale_amount DOUBLE,
    sale_date DATE
  )
  PARTITIONED BY (year INT, month INT)
  STORED AS PARQUET
""")

// Insert data with partitions
spark.sql("""
  INSERT INTO sales_partitioned PARTITION (year=2023, month=1) VALUES
  (101, 150.00, '2023-01-15'),
  (102, 200.00, '2023-01-20')
""")

// Query with partition pruning
val partitionQuery = spark.sql("""
  SELECT * FROM sales_partitioned 
  WHERE year = 2023 AND month = 1
""")

partitionQuery.show()
```

## Running the Examples

### Using Docker Hive

1. **Start Hive Docker container:**
   ```bash
   docker run -d --name hive-server -p 10000:10000 -p 10002:10002 --env SERVICE_NAME=hiveserver2 apache/hive:4.0.0
   ```

2. **Run the connectivity example:**
   ```bash
   sbt "runMain connectDockerHive"
   ```

### Using Local Hive

1. **Ensure Hive is installed and running**
2. **Update connection parameters:**
   ```scala
   val hiveHost = "localhost"
   val hivePort = "10000"
   ```

3. **Run examples:**
   ```bash
   sbt "runMain FileToHiveTable"
   ```

## Key Concepts Covered

### Hive Table Management
- **Table Creation**: DDL operations in Hive
- **Data Insertion**: Loading data into Hive tables
- **Partitioning**: Organizing data for query performance
- **File Formats**: PARQUET, ORC, TEXTFILE storage

### Docker Integration
- **Container Networking**: Connecting to Dockerized services
- **Port Mapping**: Accessing containerized Hive
- **Configuration**: Environment-specific settings

### Performance Optimization
- **Partition Pruning**: Query optimization with partitions
- **File Formats**: Choosing appropriate storage formats
- **Indexing**: Improving query performance

## Comparison with Other Systems

| Feature | Hive Integration | JDBC Connections | Kafka Integration |
|---------|------------------|------------------|-------------------|
| **Data Type** | Batch/analytical | Transactional | Streaming |
| **Storage** | HDFS/distributed | Relational DB | Message queue |
| **Use Case** | Data warehousing | OLTP integration | Event streaming |
| **API Style** | SQL-like | JDBC | Producer/consumer |

## Troubleshooting

### Common Issues

**Connection refused:**
- Check if HiveServer2 is running: `netstat -tlnp | grep 10000`
- Verify Docker container status: `docker ps`
- Check firewall settings

**Permission denied:**
- Ensure proper HDFS permissions
- Check user authentication
- Verify warehouse directory access

**Table not found:**
- Confirm database selection: `USE database_name`
- Check table existence: `SHOW TABLES`
- Verify case sensitivity

**Performance issues:**
- Enable partitioning for large datasets
- Choose appropriate file formats
- Monitor query execution plans

---

*These Scala Hive integration examples demonstrate connecting Spark with Apache Hive for data warehousing and analytical workloads.*
