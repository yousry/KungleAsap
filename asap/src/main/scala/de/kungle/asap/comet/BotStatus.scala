package de.kungle.asap.comet

import net.liftweb.common._
import net.liftweb.http._
import net.liftweb.http.js._
import net.liftweb.http.js.JsCmds._

import de.kungle.process.ProcessMaster

class BotStatus extends CometActor with Loggable{

 
  override def defaultPrefix = Full("bot")

  override def render = bind("status" -> rndStat)
  
  
  
  def rndStat = <span id="status">Wait for Sig</span>
  

  override def lowPriority : PartialFunction[Any, Unit] = {
    case BotUpdateMessage(bots) => {
      logger.info("Stats Client message received from ProcessMaster." )
      partialUpdate(SetHtml("status", <span id="status">{bots}</span>))
    }
    
  }

  override def localSetup {
    ProcessMaster ! new ProcessMaster.SubscribeBotStatus(this)
    super.localSetup
  }
  
  override def localShutdown {
    ProcessMaster ! new ProcessMaster.UnSubscribeBotStatus(this)
    super.localShutdown
    
  }
  
}
  
case class BotUpdateMessage(bots: List[(String,java.util.Date)])
