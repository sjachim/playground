package playground

import scala.actors.Actor
import scala.actors.Actor._
import scala.actors._
import scala.ref.WeakReference

object Ground extends Application { 
  println("adsf")
  val anActor = actor {
    while(true) {
      receive {
        case 'givemeachannel => new Channel
        case channel ! msg => println(channel, msg)
      }  
    }
  } 
  
  val ch: Channel[Any] = (anActor !? 'givemeachannel).asInstanceOf[Channel[Any]]
  
  ch ! 'test
  
  exit;
  
  /*
  object properties { 
    private[this] val propertyLists = new scala.collection.jcl.WeakHashMap[AnyRef, scala.collection.mutable.HashMap[Symbol, Any]]
    def apply(any: AnyRef)(name: Symbol): Any = props(any)(name) 
    def update(any: AnyRef, name: Symbol, value: Any) {  
      if (!props.isDefinedAt(any)) {
        props(any) = new scala.collection.mutable.HashMap[Symbol, Any]
      }
      props(any)(name) = value
    }
    def dump {
      println("Dump:") 
      props foreach println   
      printf("Length: %d\n", props.size)     
    }
  }
  
  var anObject = new { }
  
  properties(anObject, 'name) = "blah"
  println(properties(anObject, 'name))
  
  properties.dump

  anObject = null
  Thread.sleep(1000)
  System.gc
  Thread.sleep(1000)
  
  properties.dump
  
  1
  
  exit
  */
  
  
  import org.eclipse.swt._
import org.eclipse.swt.graphics.Color
import org.eclipse.swt.events._
import org.eclipse.swt.custom._
import org.eclipse.swt.layout._
    import org.eclipse.swt.widgets.{Composite, Display, Shell, Text, Label, Button, Event, Listener}
 
      val shell = new Shell(display)
 
      val c = new Composite(shell,SWT.NONE) 
      val layout = new GridLayout
      layout.marginTop = 10
      layout.marginLeft = 10
      layout.numColumns = 2      
        
      val label = new Label(c,SWT.NONE)
      val textControl = new Text(c,SWT.NONE)

      label.setText("ʎɐqǝ uo pɹɐoqʎǝʞ ɐ ʎnq ı ǝɯıʇ ʇsɐן ǝɥʇ sı sıɥʇ")
      shell.setText("The title �")
      textControl.setText("Text goes here")
      
      c.setLayout(layout)
      c.pack
      
 
        val b = new Button(shell, SWT.PUSH);
    b.setBackground(shell.getBackground);
    b.setText("close");
    b.pack();
    b.setLocation(10, 68);
                
    val p = new Button(shell, SWT.PUSH)
    p.setBackground(shell.getBackground)
    
        p.setText("close");
    p.pack
    p.setLocation(10, 48);

         shell.pack 
     shell.open
    
     
     import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Image;

/*
    val large = new Image(display, 128, 128);
       
    val whitePixel = large.getImageData.palette.getPixel(new RGB(255,255,255));
    large.getImageData.transparentPixel = whitePixel;

    val gc = new GC(large);
    gc.setBackground(display.getSystemColor(SWT.COLOR_BLACK));
    gc.fillArc(0, 0, 128, 128, 45, 270);
    gc.dispose();
  
    shell.setImage(new Image(display, large.getImageData, large.getImageData))

*/
           
     val a1 = actor {
      while (true) {
         receive {
           case 'ping => println("Ping")
           case (SWT.Selection, _) => display ! 'terminate
           case (SWT.Close, _) => display ! 'terminate
           case unknown => println("got unknown message: " + unknown)
         }
       }
     } 
     
     b.addListener(SWT.Selection, a1)
     p.addListener(SWT.Selection, a1) 
     shell.addListener(SWT.Close, a1)
  //   display.addListener(SWT.Dispose, a1)

}

