package playground 

import scala.actors.Actor
import scala.actors.Actor._

case class NotifyProto(newClone: Actor)
case class NotifyClone(prototype: Actor) 

object Cloningg {  
  lazy val actorClass = (actor { exit }).getClass
  def cloneActor(a: scala.actors.Actor): Actor = {
    assert(a.getClass == actorClass, "Only simple actors created with actor { } can be cloned")
    val cloned = actor(a.act)
    a ! NotifyProto(cloned) 
    cloned ! NotifyClone(a)
    cloned
  }
  implicit def intmulact(i: Int) = new {                      
    def *(a: scala.actors.Actor): Seq[Actor] = a :: ((0 until i-1) map { _ => cloneActor(a)} toList)
  }
}
                                         

