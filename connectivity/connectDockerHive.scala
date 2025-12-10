package org.example
import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

object connectDockerHive  extends App {
  // Set the log level to ERROR
  Logger.getLogger("org").setLevel(Level.ERROR)

  // Create a SparkSession
  val spark = SparkSession.builder()
    .appName("HiveExample")
    .master("local")
    .config("spark.sql.warehouse.dir", "hdfs://localhost:50070/user/hive/warehouse")
    .config("hive.metastore.uris", "thrift://localhost:9083")
    .config("spark.hadoop.dfs.datanode.address", "hdfs://localhost:50075")
    .enableHiveSupport()
    .getOrCreate()
  spark.sparkContext.setLogLevel("ERROR")

  spark.sql(" show tables ").show
  //spark.sql(" select * from  pokes ").show
  // Set the input file path
  val inputFile = "G:\\My Drive\\learn_all\\DEV_practice\\data\\CSVs\\Financial_sample.csv"

  // Read the file into a DataFrame
  val data = spark.read.format("csv").option("header", "true").load(inputFile)

  println(" input data == ")
  data.show()

  val tableName = "davin.table_from_spark"

  println(" the DataFrame to a Hive table")

  //data.write.mode("overwrite").saveAsTable(tableName)
  spark.sql(" select * from pokes ").show()


}
