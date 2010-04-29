package de.kungle.process

import net.liftweb.actor._
import _root_.net.liftweb._
import http._
import util._
import Helpers._
import net.liftweb.common._

object StrokeMaster extends LiftActor with Loggable {
  
  var subscriptionId : Long = 0
  
  protected def messageHandler = {
    case m => logger.error("Scheduler Unidentified Command: " + m)
  }

}
