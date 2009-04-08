package playground

import scala.actors.Actor
import scala.actors.Actor._

final abstract case class AbsolutelyNothing()

object Cloning {
  lazy val actorClass = (actor { exit }).getClass
  def cloneActor(a: scala.actors.Actor): Actor = {
    assert(a.getClass == actorClass, "Only simple actors created with actor { } can be cloned")
    actor(a.act)  
  }
  implicit def intmulact(i: Int) = new {                      
    def *(a: scala.actors.Actor): Seq[Actor] = a :: ((1 until i) map { _ => cloneActor(a)} toList)
  }
} 

object Swarm {  
  import Cloning._ 
  def main(args : Array[String]) : Unit = { 
    def proto = actor { 
      loop {
        react {
          case i: Int => println(Thread.currentThread, self, i); reply(i + 1)  
        }  
      }
    }
    def pool = 4 * proto 
    
  def distributor(actors: Seq[Actor]) = actor {
    lazy val actorCycle: Stream[Actor] = actors.toStream append actorCycle
    for (a <- actorCycle) {
      Thread.sleep(200)
      receive {
        case msg => a.send(msg, sender.receiver)
      }
    }
  } 
  
  
  println("ʎɐqǝ uo pɹɐoqʎǝʞ ɐ ʎnq ı ǝɯıʇ ʇsɐן ǝɥʇ sı sıɥʇ")
  
  (1 to 10) foreach (distributor(pool) !)
  
 // pool foreach (_.exit) 

    ()
  }
}