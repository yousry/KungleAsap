package de.kungle.process.business

import net.liftweb.actor._
import _root_.net.liftweb._
import http._
import util._
import Helpers._


class InfoCollector extends LiftActor with BackgroundTask{

 override var scheduleIntervall : Long = 1000 * 60 * 10 // 10 Minutes intervall
  
  
    protected def messageHandler = {
      case m => Log.error("Scheduler Unidentified Command: " + m)
    }
  
  
  
}
