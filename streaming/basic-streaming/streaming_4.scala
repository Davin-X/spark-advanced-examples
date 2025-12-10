import org.apache.log4j.{ Level, Logger }
import org.apache.spark.SparkContext
import org.apache.spark.streaming.{ Seconds, StreamingContext }

object streaming_4 extends App {
  Logger.getLogger( "org" ).setLevel( Level.ERROR )

  val sc = new SparkContext( "local[*]", "pratice_2" )
  val ssc = new StreamingContext( sc, Seconds( 2 ) )
  ssc.checkpoint( "." )
  val lines = ssc.socketTextStream( "localhost", 9900 )

  def summaryFunc( x : String, y : String ) = { ( x.toInt + y.toInt ).toString() }
  def inverseFunc( x : String, y : String ) = { ( x.toInt - y.toInt ).toString() }
  val wordCount = lines.reduceByWindow( summaryFunc, inverseFunc, Seconds( 10 ), Seconds( 4 ) )
  wordCount.print()
  ssc.start()
  ssc.awaitTermination()

}
