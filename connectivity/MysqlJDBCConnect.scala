package org.example

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

object MysqlJDBCConnect extends App {

  // Set the log level to ERROR
  Logger.getLogger("org").setLevel(Level.ERROR)

  // Create a SparkSession
  val spark = SparkSession.builder()
    .appName("HiveExample")
    .master("local")
    .enableHiveSupport()
    .getOrCreate()
  spark.sparkContext.setLogLevel("ERROR")
  
  //private val driver = "org.apache.hive.jdbc.HiveDriver"
  //private val url = "jdbc:hive2://localhost:10000/default"

  private val driver = "com.mysql.cj.jdbc.Driver"
  private val url = "jdbc:mysql://localhost:3306"

  private val username = "davin"
  private val password = "mysql"

  println(" input data reading from file ")
  val inputFile = "G:\\My Drive\\learn_all\\DEV_practice\\data\\CSVs\\Financial_sample.csv"
  // Read the file into a DataFrame
  val data = spark.read.format("csv").option("header", "true").load(inputFile)
  private val cleanedDF = data.toDF(data.columns.map(_.replaceAll("[^a-zA-Z0-9]", "")): _*)

  cleanedDF.show()
  println(" ==================================================== ")

  private val connectionProperties = new java.util.Properties()
  connectionProperties.put("user", "davin")
  connectionProperties.put("password", "mysql")

  println(" writing to a mysql table ==== ")

  cleanedDF.write
    .format("jdbc")
    .option("url", url)
    .option("driver", driver)
    .option("dbtable", "tmp.Financial_sample")
    .mode("overwrite") // or "overwrite" to overwrite existing data
    .jdbc(url, "tmp.Financial_sample", connectionProperties)


  println(" reading from the created mysql table ==== ")

  private val jdbcDF1 = spark.read
    .format("jdbc")
    .option("url", url)
    .option("driver", driver)
    .option("dbtable", "tmp.Financial_sample")
    .option("user", username)
    .option("password", password)
    .load()

  jdbcDF1.show()
  println(" ========= Done ====================== ")


}
