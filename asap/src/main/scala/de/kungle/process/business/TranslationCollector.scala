package de.kungle.process.business

import net.liftweb.actor._
import _root_.net.liftweb._
import http._
import util._
import Helpers._
import net.liftweb.common._

object TranslationCollector extends LiftActor with BackgroundTask with Loggable {
  
   override var scheduleIntervall : Long = 1 // 10 Minutes intervall
       
   protected def messageHandler = {
      case DoWork => work
      case m => logger.error("Scheduler Unidentified Command: " + m)
    }

    def work() = {
   logger.info("TranslatorCollector Started at: %s".format(now))
   logger.info("Translator finished.")
    ActorPing.schedule(InfoCollector, InfoCollector.DoWork, scheduleIntervall minutes)
 }

  
  
}
