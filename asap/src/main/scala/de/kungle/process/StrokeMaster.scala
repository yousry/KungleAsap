package de.kungle.process

import net.liftweb.actor._
import _root_.net.liftweb._
import http._
import util._
import Helpers._
import net.liftweb.common._

import de.kungle.asap.comet._
import de.kungle.asap.comet.StrokeStatus

import scala.collection.mutable.HashMap

object StrokeMaster extends LiftActor with Loggable {
  
  var subscriptionId : Long = 0
  val strokeClients = new HashMap[StrokeStatus,long]()
  
  var activeStrokes = List[String]() 
  
  protected def messageHandler = {
    case SubscribeStrokeStatus(sstat) => {
         subscriptionId += 1
         strokeClients += (sstat -> subscriptionId)
         sstat ! InitSubscriptionId(subscriptionId)
       }
    case UnSubscribeStrokeStatus(sstat) => strokeClients -= sstat
    case StrokeUpdate(sid,scmd) => logger.info("Stroke update from: " + sid + " Command: " + scmd )
    case m => logger.error("Scheduler Unidentified Command: " + m)
  }

       
  case class SubscribeStrokeStatus(sStat: StrokeStatus)
  case class UnSubscribeStrokeStatus(sStat: StrokeStatus)
  case class StrokeUpdate(sId: Long, sCmd : String)

  
}
