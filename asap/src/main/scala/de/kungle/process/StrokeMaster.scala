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
  var strokeClients = List[StrokeStatus]()
  var strokes : List[String] = List()
  
  var activeStrokes = List[String]() 
  
  protected def messageHandler = {
    case SubscribeStrokeStatus(sstat) => {
         subscriptionId += 1
         strokeClients ::= sstat
         sstat ! InitSubscriptionId(subscriptionId)
       }
    case UnSubscribeStrokeStatus(sstat) => strokeClients -= sstat
    case StrokeUpdate(sid,scmd) => {
      
      if(strokes.length > 10000) {
        strokes = List()
      }
      
      if(scmd.contains("init")) {
         strokeClients.foreach( sc => sc !  InitStrokes(sid, strokes))
      } else {
        if(scmd.contains("clear")) strokes = List()
        strokeClients.foreach(sc => sc ! AddStroke(sid, scmd))
        strokes ::= scmd
      } 
    }      
    case m => logger.error("StrokeMaster Unidentified Command: " + m)
  }
       
  case class SubscribeStrokeStatus(sStat: StrokeStatus)
  case class UnSubscribeStrokeStatus(sStat: StrokeStatus)
  case class StrokeUpdate(sId: Long, sCmd : String)
  
}
