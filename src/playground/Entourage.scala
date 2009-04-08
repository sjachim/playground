package playground 

object Entourage {

    import scala.actors._    
    import scala.actors.Actor._
    
    case class bind(name: Symbol, value: Actor)
    implicit def actor2dollar(a: scala.actors.Actor) = new { 
      object $ {
          def update(s: Symbol, v: Actor) {
            a ! bind(s, v)
          }     
      }
    }
    
    val aaa = actor { Thread.sleep(1)
      val values = new scala.collection.mutable.HashMap[Symbol, Actor]
      while(true) {
        receive {
          case bind(name, value) => values(name) = value
          case s: Symbol => reply(values(s))                                                            
        }
      } 
    } 
    /*
    implicit def symb2setSlotBuilder(s: Symbol) = new {
      def update() setSlot(s, )
    }
    */    
    
    aaa $ 'asd = actor {
      while(true) {
        receive {
          case x => println(x) 
        }
      }
    }
    (aaa !? 'asd).asInstanceOf[Actor] ! 12345 
    
    println(aaa !? 'asd)
    
    case class TOexpr(a: Int, a2: Int) 
    implicit def any2TOexr(a: Int) = new {
      def TO(a2: Int) = TOexpr(a, a2)
    } 
    object X {
      object FOR {
        def update(s: Symbol, to: TOexpr) = {
          println(s, to)                  
        } 
      }
    } 
    
    X FOR 'I = 10 TO 20
       
    val a = actor {
      loop {
        react {
          case x => println(x); reply(x.toString + x)
        }
      }
    }
    
    class Forwarder extends scala.actors.Actor {
      var bind: Option[Pair[scala.actors.AbstractActor, scala.actors.AbstractActor]] = None 
      def act() {
        loop { 
          bind match {
            case None =>
            case Some((to, fwdr)) => react {
              case x => to.send(x, fwdr)
            }
          } 
        }
      }
      def >>(actor: scala.actors.Actor) = {
        val fwdr = new Forwarder
        bind = Some((actor, fwdr))
        start
        fwdr
      }      
    }
    
    implicit def seq2sender[A](seq: Seq[A]) = new  {
      def >>(actor: scala.actors.Actor) = {
        val f = new Forwarder
        for (msg <- seq)
          actor.send(msg, f)
        f
      }
    }
    
    val a2 = actor {
      loop {
        react {
          case x => println(x)
        }
      }
    }
    
    (1 to 10) >> a >> actor {
      loop {
        react {
          case x => reply(x + "___")
        }
      }
    } >> actor {
      loop {
        react {
          case x => println("!" + x)
        }
      }
    }     
   
    println("3434")
}
