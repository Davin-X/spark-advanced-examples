import org.apache.log4j.{ Level, Logger }
import org.apache.spark.SparkContext
import org.apache.spark.streaming.{ Seconds, StreamingContext }

object streaming_3 extends App {
  Logger.getLogger( "org" ).setLevel( Level.ERROR )
  val sc = new SparkContext( "local[*]", "pratice_1" )
  val ssc = new StreamingContext( sc, Seconds( 2 ) )
  ssc.checkpoint( "." )
  val lines = ssc.socketTextStream( "localhost", 9999 )
  // words is a transform Dstream
  val wordCount = lines.flatMap( x => x.split( " " ) )
    .map( x => ( x, 1 ) )
    .reduceByKeyAndWindow( ( x, y ) => x + y, ( x, y ) => x - y, Seconds( 10 ), Seconds( 4 ) ) //.reduceByKey(_ + _)
    .filter( x => x._2 > 0 )
  wordCount.print()
  ssc.start()
  ssc.awaitTermination()
}
