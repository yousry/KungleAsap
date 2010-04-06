package de.kungle.process.business

import net.liftweb.actor._
import _root_.net.liftweb._
import http._
import util._
import Helpers._


class InfoCollector extends LiftActor{

    protected def messageHandler = {
      case m => Log.error("Scheduler Unidentified Command: " + m)
    }
  
  
  
}
