package de.kungle.asap.comet

import scala.xml._

import net.liftweb.common._
import net.liftweb.http._
import net.liftweb.http.js._
import net.liftweb.http.js.JsCmds._


class WhatHappensNextStatus extends CometActor with Loggable {
  
  override def defaultPrefix = Full("whn")
  val eventEndTime =  new java.util.Date(110,4,25)
  
  def render = bind(
    "time" -> rndTime,
    "messages" -> <span/>
  )

  def isActive : Boolean = if(eventEndTime.getTime > new (java.util.Date).getTime ) true else false
  
  def rndTime = {
    val minutes = 1000 * 60
    val hours = minutes * 60
    
    var duration = eventEndTime.getTime - new (java.util.Date).getTime
    if(duration < 0) duration = 0
    
    val remainingHours = duration / hours
    val remainingMinutes = (duration - remainingHours * hours) / minutes
    
    Text(remainingHours+":"+remainingMinutes)
  }

  
  def pushPrediction() = ()
  
  override def lowPriority : PartialFunction[Any, Unit] = {
    case SendPrediction => pushPrediction()
    case _ => logger.info("WhatHappensNextStatus unknown commend.")
  }
  
}

case class SendPrediction
