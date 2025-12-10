package org.example

import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

object FileToHiveTable {

  def main(args: Array[String]): Unit = {
    // Set the log level to ERROR
    Logger.getLogger("org").setLevel(Level.ERROR)

    // Create a SparkSession
    val spark = SparkSession.builder
      .master("local")
      .appName("Spark Hive Integration")
      .config("spark.sql.warehouse.dir", "/user/hive/warehouse")
      .config("hive.metastore.uris", "thrift://localhost:9083")
      .enableHiveSupport()
      .getOrCreate()

    spark.sparkContext.setLogLevel("ERROR")
    // Set the input file path
    val inputFile = "G:\\My Drive\\learn_all\\DEV_practice\\data\\CSVs\\Financial_sample.csv"

    // Read the file into a DataFrame
    val data = spark.read.format("csv").option("header", "true").load(inputFile)

    println(" input data == ")
    data.show()
    val tableName = "my_table"
    // Get the Hadoop file system
    val fs = FileSystem.get(spark.sparkContext.hadoopConfiguration)

    // Specify the directory path to delete
    val dirPath = s"/user/hive/warehouse/$tableName"

    // Create a Path object
    val path = new Path(dirPath)

    // Delete the directory
    fs.delete(path, true)

    println(" the DataFrame to a Hive table")

    data.write.mode("overwrite").saveAsTable(tableName)

    println(" table's data == ")

    println(" Print the contents of the Hive table")

    spark.sql(s"SELECT * FROM $tableName").show()

    // Stop the SparkSession
    // spark.stop()
  }
}


