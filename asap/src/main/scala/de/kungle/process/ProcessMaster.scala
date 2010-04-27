package de.kungle.process

import net.liftweb.actor._
import _root_.net.liftweb._
import http._
import util._
import Helpers._
import net.liftweb.common._
import scala.collection.mutable.HashMap

import de.kungle.asap.comet._
import de.kungle.asap.comet.BotStatus

object ProcessMaster extends LiftActor with Loggable{

  val bots = new HashMap[String,java.util.Date]()
  
  var botStats = List[BotStatus]()
  var lastTalk = (new java.util.Date, "Booting Process Master")
  
     protected def messageHandler = {
       case SubscribeBotStatus(bs) => {
         botStats ::= bs
         bs ! BotUpdateMessage(bots.toList)
         bs ! BotTalkingMessage(lastTalk._1,lastTalk._2)
         logger.info("Message send to newcommer.")
       }
       case UnSubscribeBotStatus(bs) => botStats -= bs
       case UpdateStatus(bot) => {
         bots += (bot -> now)
         botStats.foreach(b => b ! BotUpdateMessage(bots.toList))
         logger.info("Status of Bot: " + bot + " updated.")
         logger.info("Message send to: " + botStats.length + " stats.")
       }
         case UpdateMessage(message) => {  
         logger.info("New Message received: " + message)  
         lastTalk = (new java.util.Date, message)
         botStats.foreach(b => b ! BotTalkingMessage(lastTalk._1,lastTalk._2))
       }
       case m => logger.error("Scheduler Unidentified Command: " + m)
    }

     case class UpdateMessage(message: String)
     case class UpdateStatus(botName: String)
     case class SubscribeBotStatus(bStat: BotStatus)
     case class UnSubscribeBotStatus(bStat: BotStatus)
     case object InformStatClients
}
