package de.kungle.asap.comet

import net.liftweb.common._
import net.liftweb.http._
import net.liftweb.http.js._
import net.liftweb.http.js.JsCmds._


class BotStatus extends CometActor {
  
  override def render = <span>Take a breath.</span>
  
  override def defaultPrefix = Full("bot")

  override def lowPriority : PartialFunction[Any, Unit] = {
    case BotUpdateMessage(bots) => {
      partialUpdate(SetHtml("status", <span>Update</span>))
    }
    
  }

  case class BotUpdateMessage(bots: List[(String,java.util.Date)])

  
}
