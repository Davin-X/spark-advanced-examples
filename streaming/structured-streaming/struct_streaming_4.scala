
import org.apache.log4j.{ Level, Logger }
import org.apache.spark.sql.{ SparkSession, functions }
import org.apache.spark.sql.functions.{ col, sum, window }
import org.apache.spark.sql.streaming.Trigger
import org.apache.spark.sql.types.{ IntegerType, StringType, StructField, StructType }

object tumbling_window extends App {
  Logger.getLogger( "org" ).setLevel( Level.ERROR )

  val spark = SparkSession.builder()
    .master( "local[2]" )
    .appName( "streaming_app" )
    .config( "spark.sql.shuffle.partitions", 3 )
    .config( "spark.streaming.stopGracefullyOnShutdown", "true" )
    //.config( "spark.sql.streaming.schemaInference", "true" )
    .getOrCreate()
  val orderSchema = StructType( List(

    StructField( "order_id", IntegerType ),
    StructField( "order_date", StringType ),
    StructField( "order_customer_id", IntegerType ),
    StructField( "order_status", StringType ),
    StructField( "amount", IntegerType ) ) )

  // read the data from socket
  val orderDf = spark.readStream
    .format( "socket" )
    .option( "host", "localhost" )
    .option( "port", "12344" )
    .load

  // orderDf.printSchema()

  val valueDf = orderDf.select( functions.from_json( col( "value" ), orderSchema ).alias( "value" ) )
  // valueDf.printSchema()
  val refinedOrderDf = valueDf.select( "value.*" )
  refinedOrderDf.printSchema()
  val windowAggDf = refinedOrderDf
    .groupBy( window( col( "order_date" ), "15 minute" ) )
    .agg( sum( "amount" )
      .as( "totalInvoice" ) )
  // windowAggDf.printSchema()
  val outputDf = windowAggDf.select( "window.start", "window.end", "totalInvoice" )
  val ordersQuery = outputDf.writeStream.format( "console" )
    .outputMode( "update" )
    .option( "checkpointLocation", "checkpoint-1" )
    .trigger( Trigger.ProcessingTime( "15 second" ) )
    .start()
  ordersQuery.awaitTermination()

}
