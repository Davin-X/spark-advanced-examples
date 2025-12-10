import org.apache.log4j.{ Level, Logger }
import org.apache.spark.sql.SparkSession

object struct_streaming extends App {
  Logger.getLogger( "org" ).setLevel( Level.ERROR )

  val spark = SparkSession.builder().master( "local[2]" )
    .appName( "streaming_app" )
    .config( "spark.sql.shuffle.partitions", 3 )
    .config( "spark.streaming.stopGracefullyOnShutdown", "true" )
    .getOrCreate()

  //read from  the stream
  val lineDf = spark.readStream.format( "socket" )
    .option( "host", "localhost" )
    .option( "port", "12345" )
    .load
  //lineDf.printSchema()
  // 2 process
  val wordsDf = lineDf.selectExpr( "explode(split(value,' ')) as word " )
  val countDf = wordsDf.groupBy( "word" ).count
  // 3 write to the sink
  val wordCountQuery = countDf.writeStream
    .format( "console" )
    .outputMode( "complete" )
    .option( "checkpointLocation", "checkpoint-location1" )
    .start
  wordCountQuery.awaitTermination()
}
