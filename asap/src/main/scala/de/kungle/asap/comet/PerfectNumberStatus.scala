package de.kungle.asap {
package comet {

import scala.xml._

import net.liftweb.common._
import net.liftweb.http._
import net.liftweb.http.js._
import net.liftweb.http.js.JsCmds._
import net.liftweb.http.js.JE._

import de.kungle.process.PerfectNumberMaster
import de.kungle.asap.snippet.Crunchy

class PerfectNumberStatus extends CometActor with Loggable{
  
   
  override def defaultPrefix = Full("pfn")
  
  override def render = <h1>Perfect Number Crunching Activated</h1>
  
  override def lowPriority : PartialFunction[Any, Unit] = {
    case DoCrunch(c) => {      
      logger.info("+++++++++++++++Crunch Command send to Client")
      partialUpdate(OnLoad(JsRaw("findFactors('"+ c.toJson +"');")))
      }
    case m => logger.error("PerfectNumberStatus Unidentified Command: " + m)
  }
     
  override def localSetup {
     logger.info("PerfectNumberStatus client setup")
     PerfectNumberMaster ! new PerfectNumberMaster.SubscribePerfectNumberMaster(this)
     super.localSetup
  }
  
   override def localShutdown {
    PerfectNumberMaster ! new PerfectNumberMaster.UnSubscribePerfectNumberMaster(this)
    super.localShutdown
  }

}

case class DoCrunch(crunchy: Crunchy)

}}