package de.kungle.process

import net.liftweb.actor._
import _root_.net.liftweb._
import http._
import util._
import Helpers._
import net.liftweb.common._
import scala.collection.mutable.HashMap


object ProcessMaster extends LiftActor with Loggable{

  val bots = new HashMap[String,java.util.Date]()
  
  
     protected def messageHandler = {
       case UpdateStatus(bot) => {
         bots += (bot -> now)
         logger.info("Status of Bot: " + bot + " updated.")
       }
       case m => logger.error("Scheduler Unidentified Command: " + m)
    }


     case class UpdateStatus(botName: String)
}
