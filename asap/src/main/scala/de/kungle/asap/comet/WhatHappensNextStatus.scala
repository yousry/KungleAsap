package de.kungle.asap.comet

import scala.xml._

import net.liftweb.util._
import net.liftweb.common._
import net.liftweb.http._
import net.liftweb.http.js._
import net.liftweb.http.js.JsCmds._
import Helpers._


import net.liftweb.http.js.JE._
import net.liftweb.http.js.JsCmds._

import net.liftweb.http.SessionVar

import  net.liftweb.mapper._

import de.kungle.asap.model.{Comment => KungleComment, User}

import de.kungle.asap.snippet.actualLanguage

object WhatHappensNextObj extends SessionVar[Box[WhatHappensNextStatus]](Empty)
object WhatHappensNextInit extends SessionVar[Boolean](true)


class WhatHappensNextStatus extends CometActor with Loggable {
  
  
  override def defaultPrefix = Full("whn")
  val eventEndTime =  new java.util.Date(110,4,31)
  
  // Läuft bis zum 14 Juni
  
  var activeMessage = 0 
  
  // during construction start a self awake timer
  ActorPing.schedule(this, Revoke, 2 seconds)
  
  // Everyone in this session should know me
  WhatHappensNextObj(Full(this))
  
  def render = bind(
    "evntTime" -> <span id="evntTime">{rndTime}</span>,
    "pillar" -> <span id="pillar">{rndPillar}</span>
  )

  def isActive : Boolean = if(eventEndTime.getTime > new (java.util.Date).getTime ) true else false
  
  def rndPillar = {
   val cmts = KungleComment.findAll(OrderBy(KungleComment.id, Descending),  MaxRows(5)) drop activeMessage
   activeMessage = if(activeMessage == 4) 0 else activeMessage + 1
     
   if(cmts.isEmpty) {
     activeMessage = 0
     <span></span> 
   }else {
     val cmt = cmts.head
     val language = actualLanguage.get
     var commentText = language match {
       case "french" => cmt.summary_french
       case "german" => cmt.summary_german
       case _ => cmt.summary_english
     }
     
     var author = if( cmt.author == Null) 
    	 			"Anonymous" 
                  else {
                    User.find(By(User.id, cmt.author)) match {
                      case Full(a) => a.userName.is
                      case _ => "Anonymous"
                    }
                  }
     
     <span>{author}:{Text(commentText)}</span>
   }
  }
  
  
  
  def rndTime = {
    val minutes = 1000 * 60
    val hours = minutes * 60
    
    var duration = eventEndTime.getTime - new (java.util.Date).getTime
    if(duration < 0) duration = 0
    
    val remainingHours = duration / hours
    val remainingMinutes = (duration - remainingHours * hours) / minutes
    
    
    Text("%d:%02d".format(remainingHours, remainingMinutes))
  }

  
  def sendPrediction = activeMessage = 0
  
  def revoke = {
    ActorPing.schedule(this, Revoke, 10 seconds)
          
    if(WhatHappensNextInit.get == true && isActive ) {  
      WhatHappensNextInit(false)
      // open the dialog
      partialUpdate(
        JsRaw("$(\"#whatNext\").dialog({height: 400, width: 660, position:[500 ,180], resizable: false, show: 'blind'});")
      )
    }
    
    partialUpdate(
      SetHtml("pillar", <span id="pillar">{rndPillar}</span>) &
      SetHtml("evntTime", <span id="evntTime">{rndTime}</span>) &
      JsRaw("$(\"#pillarDiv\").show(\"blind\",{direction: \"right\", distance: 40, times: 5},\"normal\");")
    )
  }
  
  override def lowPriority : PartialFunction[Any, Unit] = {
    case Revoke => revoke
    case SendPrediction => sendPrediction
    case _ => logger.info("WhatHappensNextStatus unknown commend.")
  }
}
  
case class Revoke()
case class SendPrediction()
