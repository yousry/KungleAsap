package de.kungle.asap.comet

import scala.xml._

import net.liftweb.common._
import net.liftweb.http._
import net.liftweb.http.js._
import net.liftweb.http.js.JsCmds._

import de.kungle.asap.snippet.WaveJasonHandler
import de.kungle.asap.snippet.myStrokeId

import de.kungle.process.StrokeMaster

class StrokeStatus extends CometActor with Loggable{
  
  override def defaultPrefix = Full("stroke")
   
  override def render = bind(
    "status" -> status
  )

    def status = <span id="status">{jsonStat}</span>
    def jsonStat : NodeSeq = <p>{ Text("Test: " + (WaveJasonHandler.get) ) }</p>
      
    override def localSetup {
     StrokeMaster ! new StrokeMaster.SubscribeStrokeStatus(this)
     super.localSetup
  }
  
   override def localShutdown {
    StrokeMaster ! new StrokeMaster.UnSubscribeStrokeStatus(this)
    super.localShutdown
  }
  
     
   override def lowPriority : PartialFunction[Any, Unit] = {
     case InitSubscriptionId(newId) => {
       myStrokeId(Full(newId))
     }
     
   }

   
}

   case class InitSubscriptionId(id: Long)    
