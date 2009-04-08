package playground

import scala.actors.Actor
import scala.actors.Actor._
import scala.actors._

case class NextWithin(timeout: Int)

object ManyChannels extends scala.Application {  
  abstract sealed class BasePipe {
    lazy val handler: Actor = actor {
      loop {
        react(body)
      }
    }
    val body: PartialFunction[Any, Unit] 
  }
  case class Pipe(nextActor: Actor, nextPipe: BasePipe) extends BasePipe {
    val body: PartialFunction[Any, Unit] = { m: Any => m match { 
        case msg => println(msg, self, nextActor, nextPipe.handler); nextActor.send(msg, nextPipe.handler)
      }
    }             
  }
  case class EndPipe() extends BasePipe {
    val body: PartialFunction[Any, Unit] = { m: Any => m match {
          case NextWithin(timeout) => reply { 
            receiveWithin(timeout) { 
              case msg if !msg.isInstanceOf[NextWithin] => msg
            } 
          }
      }    
    }
  }
  class Pipeline(val firstPipe: BasePipe) {
    override def toString = "Pipeline(" + firstPipe + "}" 
    def !(msg: Any) = firstPipe.handler ! msg
    private def getEndPipe(pipe: BasePipe): EndPipe = pipe match {
      case ep: EndPipe => ep
      case Pipe(_, next) => getEndPipe(next)
    }
   def results[T](howMany: Int, timeout: Int) = new Iterator[T] {
      val Undefined = new Object;
      var givenSoFar = 0 
	  private var current: Any = Undefined
	  private def lookAhead = {
	    if (current == Undefined) current = getEndPipe(firstPipe).handler !? NextWithin(timeout)
	    current
	  }
	
	  def hasNext: Boolean = {
	    if (givenSoFar >= howMany) 
          false
        else
          lookAhead match {
	        case TIMEOUT => false
            case x => true
	      }
      } 
	
	  def next: T = lookAhead match {
	    case x => current = Undefined; givenSoFar += 1; x.asInstanceOf[T]
	  }
    }

  }
  object Pipeline {
    class PipeBuilder(actors: List[Actor]) {
      def -->(a: Actor): PipeBuilder = {
        new PipeBuilder(a :: actors)
      }         
      def -->(o: output.type): Pipeline = { new Pipeline(Build(actors.reverse)) }
      private def Build(actors: List[Actor]): BasePipe = actors match {
        case a :: rest => Pipe(a, Build(rest)) 
        case Nil => EndPipe()
      }
      
    }
    object input extends PipeBuilder(Nil) 
    object output 
  }
  
  override def main(args: Array[String]) {
    
  import Pipeline._
  
  
  val pipeline = input --> 
    actor { loop { react { case x => reply(x.toString + "AAA") } } } --> 
    actor { loop { react { case x => reply(x.toString + "BBB") } } } --> output
  
  //println(pipeline)
  
    (1 to 10) foreach (pipeline !)

    Thread.sleep(1000) 
    
  pipeline.results(10, 10) foreach println
    ()
  }

}
