package playground

import scala.actors.Actor._
import scala.actors.TIMEOUT
import scala.actors.Actor

import org.eclipse.swt._
import org.eclipse.swt.graphics.Color 
import org.eclipse.swt.events._
import org.eclipse.swt.custom._
import org.eclipse.swt.layout._
import org.eclipse.swt.widgets.{Composite, Display, Shell, Text, Label, Button, Event, Listener}

class Application {
  def main(args : Array[String]) : Unit = {
    init 
    run    
  }  
  def init {} 
  def run {
    while (true) {
      if (!display.readAndDispatch)
        display.sleep
      
      receiveWithin(100) {
        case 'terminate => display.dispose; self.exit
        case TIMEOUT =>                                                   
        case unknown => println("got unknown message: " + unknown)
      }      
    }
  }
  lazy val display = new Display 
  implicit def actor2listener(a: Actor) = new Listener {
    def handleEvent(e: Event) {
      println("sending: " + e)
      a ! (e.`type`, e.widget)
    }  
  } 
  private[this] val displayActor = self
  implicit def display2actor(d: Display): Actor = displayActor
}
