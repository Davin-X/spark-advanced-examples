
import org.apache.log4j.{ Level, Logger }
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.streaming.Trigger

object streaming_file extends App {
  Logger.getLogger( "org" ).setLevel( Level.ERROR )

  val spark = SparkSession.builder()
    .master( "local[2]" )
    .appName( "streaming_app" )
    .config( "spark.sql.shuffle.partitions", 3 )
    .config( "spark.streaming.stopGracefullyOnShutdown", "true" )
    .config( "spark.sql.streaming.schemaInference", "true" )
    .getOrCreate()

  // 1. read from file source
  val ordersDf = spark.readStream
    .format( "json" )
    .option( "path", "inputfolder" )
    .option( "maxFilesPerTrigger", 1 )
    .load
  //lineDf.printSchema()
  // 2 process
  ordersDf.createOrReplaceTempView( "orders" )
  val completedOrder = spark.sql( "select * from orders where order_status ='COMPLETE' " )
  // 3 write to the sink
  val ordersQuery = completedOrder.writeStream
    .format( "json" )
    .outputMode( "append" )
    .option( "path", "outputfolder" )
    .option( "checkpointLocation", "checkpoint-location" )
    .trigger( Trigger.ProcessingTime( "30 seconds" ) )
    .start
  ordersQuery.awaitTermination()
}
