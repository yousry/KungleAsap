package de.kungle.asap.comet

import scala.xml._

import net.liftweb.common._
import net.liftweb.http._
import net.liftweb.http.js._
import net.liftweb.http.js.JsCmds._


import de.kungle.process.ProcessMaster

class BotStatus extends CometActor with Loggable{

 
  override def defaultPrefix = Full("bot")

  override def render = bind("status" -> rndStat)
 
  var latestBots = List[(String, java.util.Date)]()
  
  def rndStat = <span id="status">{plotBots}</span>
    
    def plotBots : NodeSeq = if(latestBots.length == 0) {
      Text("Waiting for ProcessMaster sync.")
    } else {
      <table>
      <tr><th>Bot</th><th>Last Execution</th></tr>
      {latestBots.map(x => <tr><td>{x._1}</td><td>{x._2}</td></tr>  )}
      </table>
    }
  
  override def lowPriority : PartialFunction[Any, Unit] = {
    case BotUpdateMessage(bots) => {
      logger.info("Stats Client message received from ProcessMaster." )
      latestBots = bots
      partialUpdate(SetHtml("status", <span id="status">{plotBots}</span>))
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
