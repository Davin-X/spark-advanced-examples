import org.apache.log4j.{ Level, Logger }
import org.apache.spark.SparkContext
import org.apache.spark.streaming.{ Seconds, StreamingContext }

object streaming_2 extends App {
  Logger.getLogger( "org" ).setLevel( Level.ERROR )
  val sc = new SparkContext( "local[*]", "pratice_1" )
  val ssc = new StreamingContext( sc, Seconds( 5 ) )
  val lines = ssc.socketTextStream( "localhost", 9999 )

  ssc.checkpoint( "." )
  def updateFunc( newValues : Seq[ Int ], previousState : Option[ Int ] ) : Option[ Int ] = {
    val newCount = previousState.getOrElse( 0 ) + newValues.sum
    Some( newCount )
  }

  // words is a transform Dstream
  val words = lines.flatMap( x => x.split( " " ) )
  val pairs = words.map( x => ( x, 1 ) )
  val wordCounts = pairs.updateStateByKey( updateFunc )
  // val wordCounts = pairs.reduceByKey( _ + _ )
  wordCounts.print()
  ssc.start()
  ssc.awaitTermination()
}
