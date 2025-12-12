# Scala JDBC/MySQL Integration Examples

> **Note**: This is a "notebook-style" markdown file showing Scala JDBC/MySQL integration code examples. To run these examples, use SBT or your preferred Scala IDE.

## Setup and Dependencies

Add to your `build.sbt`:

```scala
libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "3.5.0",
  "org.apache.spark" %% "spark-sql" % "3.5.0",
  "mysql" % "mysql-connector-java" % "8.0.33"
)
```

## 1. MysqlJDBCConnect.scala - Basic MySQL JDBC Connection

**Cell 1: Import Statements and Configuration**

```scala
import org.apache.spark.sql.SparkSession
import java.util.Properties

// MySQL connection configuration
val mysqlHost = "localhost"
val mysqlPort = "3306"
val mysqlDatabase = "testdb"
val mysqlUser = "root"
val mysqlPassword = "password"

// JDBC URL
val jdbcUrl = s"jdbc:mysql://$mysqlHost:$mysqlPort/$mysqlDatabase?useSSL=false&allowPublicKeyRetrieval=true"

// Connection properties
val connectionProperties = new Properties()
connectionProperties.put("user", mysqlUser)
connectionProperties.put("password", mysqlPassword)
connectionProperties.put("driver", "com.mysql.cj.jdbc.Driver")
```

**Cell 2: Create Spark Session**

```scala
// Create Spark session
val spark = SparkSession.builder()
  .appName("MySQLJDBCConnection")
  .config("spark.driver.extraClassPath", "/path/to/mysql-connector-java-8.0.33.jar")
  .getOrCreate()

import spark.implicits._
import spark.sql

println(s"Spark version: ${spark.version}")
println("Spark session created for MySQL integration")
```

**Cell 3: Test MySQL Connection**

```scala
// Test connection by reading a simple query
try {
  val testDF = spark.read
    .jdbc(jdbcUrl, "(SELECT 1 as test_column) as test_table", connectionProperties)
  
  testDF.show()
  println("✅ MySQL connection successful!")
  
} catch {
  case e: Exception =>
    println(s"❌ MySQL connection failed: ${e.getMessage}")
    println("Make sure MySQL is running and credentials are correct")
}
```

**Cell 4: Create Sample Data and Insert**

```scala
// Create sample data
val sampleData = Seq(
  (1, "Alice Johnson", "Engineering", 75000.0, "2023-01-15"),
  (2, "Bob Smith", "Marketing", 65000.0, "2023-02-20"),
  (3, "Charlie Brown", "Engineering", 80000.0, "2023-03-10"),
  (4, "Diana Wilson", "HR", 60000.0, "2023-04-05")
)

// Convert to DataFrame
val employeesDF = sampleData.toDF("id", "name", "department", "salary", "hire_date")
employeesDF.show()

// Insert into MySQL (create table first in MySQL)
// Note: Table must exist in MySQL before running this
/*
employeesDF.write
  .mode("append")
  .jdbc(jdbcUrl, "employees", connectionProperties)

println("Data inserted into MySQL successfully!")
*/
```

**Cell 5: Read Data from MySQL**

```scala
// Read data from MySQL table
val mysqlDF = spark.read
  .jdbc(jdbcUrl, "employees", connectionProperties)

mysqlDF.show()
println(s"Read ${mysqlDF.count()} rows from MySQL")

// Perform analytics
val deptStats = mysqlDF.groupBy("department")
  .agg(
    count("*").alias("employee_count"),
    avg("salary").alias("avg_salary"),
    max("salary").alias("max_salary")
  )
  .orderBy(desc("avg_salary"))

deptStats.show()
```

## 2. AppConfigs.scala - Configuration Management

**Cell 1: Configuration Class Definition**

```scala
import com.typesafe.config.{Config, ConfigFactory}
import scala.collection.JavaConverters._

// Application configuration class
class AppConfigs(configFile: String = "application.conf") {
  
  // Load configuration
  private val config: Config = ConfigFactory.load(configFile)
  
  // Database configuration
  object Database {
    private val dbConfig = config.getConfig("database")
    
    val host: String = dbConfig.getString("host")
    val port: Int = dbConfig.getInt("port")
    val name: String = dbConfig.getString("name")
    val user: String = dbConfig.getString("user")
    val password: String = dbConfig.getString("password")
    val maxConnections: Int = dbConfig.getInt("maxConnections")
    
    // Construct JDBC URL
    val jdbcUrl: String = s"jdbc:mysql://$host:$port/$name?useSSL=false"
    
    // Connection properties
    val connectionProperties: java.util.Properties = {
      val props = new java.util.Properties()
      props.put("user", user)
      props.put("password", password)
      props.put("driver", "com.mysql.cj.jdbc.Driver")
      props.put("maxConnections", maxConnections.toString)
      props
    }
  }
  
  // Spark configuration
  object Spark {
    private val sparkConfig = config.getConfig("spark")
    
    val appName: String = sparkConfig.getString("appName")
    val master: String = sparkConfig.getString("master")
    val executorMemory: String = sparkConfig.getString("executorMemory")
    val driverMemory: String = sparkConfig.getString("driverMemory")
  }
  
  // Application settings
  object App {
    private val appConfig = config.getConfig("app")
    
    val batchSize: Int = appConfig.getInt("batchSize")
    val retryCount: Int = appConfig.getInt("retryCount")
    val timeoutSeconds: Int = appConfig.getInt("timeoutSeconds")
  }
}

// Companion object for easy access
object AppConfigs {
  def apply(configFile: String = "application.conf"): AppConfigs = new AppConfigs(configFile)
}
```

**Cell 2: Configuration File Example**

Create `src/main/resources/application.conf`:

```hocon
# Application Configuration
app {
  batchSize = 1000
  retryCount = 3
  timeoutSeconds = 30
}

# Database Configuration
database {
  host = "localhost"
  port = 3306
  name = "analytics_db"
  user = "spark_user"
  password = "secure_password"
  maxConnections = 10
}

# Spark Configuration
spark {
  appName = "MySQLIntegrationApp"
  master = "local[*]"
  executorMemory = "2g"
  driverMemory = "1g"
}
```

**Cell 3: Using Configuration in Application**

```scala
import org.apache.spark.sql.SparkSession

// Load configuration
val appConfig = AppConfigs()

// Create Spark session with configuration
val spark = SparkSession.builder()
  .appName(appConfig.Spark.appName)
  .config("spark.executor.memory", appConfig.Spark.executorMemory)
  .config("spark.driver.memory", appConfig.Spark.driverMemory)
  .getOrCreate()

// Use database configuration
val jdbcUrl = appConfig.Database.jdbcUrl
val connectionProps = appConfig.Database.connectionProperties

println(s"Connecting to database: ${appConfig.Database.host}:${appConfig.Database.port}")
println(s"JDBC URL: $jdbcUrl")

// Read data using configuration
val dataDF = spark.read
  .jdbc(jdbcUrl, "user_events", connectionProps)

println(s"Loaded ${dataDF.count()} records from MySQL")

// Process with configured batch size
dataDF.groupBy("event_type")
  .count()
  .orderBy(desc("count"))
  .show(appConfig.App.batchSize)
```

## Running the Examples

### MySQL Setup

1. **Install MySQL:**
   ```bash
   # macOS
   brew install mysql
   brew services start mysql
   
   # Ubuntu
   sudo apt update
   sudo apt install mysql-server
   sudo systemctl start mysql
   ```

2. **Create database and user:**
   ```sql
   CREATE DATABASE testdb;
   CREATE USER 'spark'@'localhost' IDENTIFIED BY 'password';
   GRANT ALL PRIVILEGES ON testdb.* TO 'spark'@'localhost';
   FLUSH PRIVILEGES;
   ```

3. **Create sample table:**
   ```sql
   USE testdb;
   CREATE TABLE employees (
     id INT PRIMARY KEY,
     name VARCHAR(100),
     department VARCHAR(50),
     salary DECIMAL(10,2),
     hire_date DATE
   );
   ```

### Running Scala Examples

1. **Download MySQL connector:**
   ```bash
   # Download and place in project lib directory
   wget https://repo1.maven.org/maven2/mysql/mysql-connector-java/8.0.33/mysql-connector-java-8.0.33.jar
   ```

2. **Run the JDBC example:**
   ```bash
   sbt "runMain MysqlJDBCConnect"
   ```

3. **For configuration example:**
   ```bash
   # Create application.conf file first
   sbt "runMain AppConfigs"
   ```

## Key Concepts Covered

### JDBC Integration
- **Connection Management**: Properties and connection pooling
- **Data Types Mapping**: Spark to MySQL type conversion
- **Batch Operations**: Efficient bulk data transfer
- **Error Handling**: Connection failures and timeouts

### Configuration Management
- **External Configuration**: HOCON config files
- **Type Safety**: Scala case classes for configuration
- **Environment Separation**: Different configs for dev/prod
- **Validation**: Configuration validation and defaults

### Production Patterns
- **Connection Pooling**: Managing database connections
- **Retry Logic**: Handling transient failures
- **Monitoring**: Logging and metrics collection
- **Security**: Credential management and encryption

## Performance Optimization

### Connection Optimization
```scala
// Use connection pooling
connectionProperties.put("maxConnections", "10")
connectionProperties.put("connectionTimeout", "30000")

// Partition data for parallel reads
val predicates = Array(
  "id >= 1 AND id < 10000",
  "id >= 10000 AND id < 20000",
  "id >= 20000 AND id < 30000"
)

spark.read
  .jdbc(jdbcUrl, "large_table", predicates, connectionProperties)
```

### Query Optimization
```scala
// Push down filters to database
val filteredDF = spark.read
  .jdbc(jdbcUrl, "employees", connectionProperties)
  .filter("salary > 50000")  // This filter is pushed to MySQL
  .select("name", "department", "salary")
```

## Troubleshooting

### Common Issues

**ClassNotFoundException:**
- Ensure MySQL connector JAR is in classpath
- Check JAR version compatibility
- Verify driver class name

**Connection refused:**
- Check MySQL service status: `sudo systemctl status mysql`
- Verify port accessibility: `telnet localhost 3306`
- Confirm user permissions in MySQL

**Authentication failed:**
- Verify username/password in connection properties
- Check user grants: `SHOW GRANTS FOR 'user'@'localhost';`
- Ensure user exists: `SELECT User FROM mysql.user;`

**Performance issues:**
- Enable query pushdown filters
- Use appropriate partitioning strategies
- Monitor connection pool usage
- Consider connection pooling libraries

## Comparison with Other Integrations

| Feature | JDBC/MySQL | Hive Integration | Kafka Integration |
|---------|------------|------------------|-------------------|
| **Data Model** | Relational | Distributed files | Event streaming |
| **Query Language** | SQL | HiveQL | N/A |
| **ACID Compliance** | Full ACID | Eventual consistency | At-least-once |
| **Use Case** | OLTP integration | Data warehousing | Event processing |
| **Performance** | Low latency | High throughput | Real-time |

---

*These Scala JDBC/MySQL integration examples demonstrate connecting Spark with relational databases for transactional and analytical workloads.*
