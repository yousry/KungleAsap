package de.kungle.process.business

import net.liftweb.actor._
import _root_.net.liftweb._
import http._
import util._
import Helpers._
import net.liftweb.common._
import de.kungle.process._
import de.kungle.process.info._

object InfoCollector extends LiftActor with BackgroundTask with Loggable {

 override var scheduleIntervall : Long = 10 // 10 Minutes intervall
  
  
    protected def messageHandler = {
      case DoWork => work
      case m => logger.error("Scheduler Unidentified Command: " + m)
    }
  

 def work() = {
   logger.info("Info Collector started at: %s".format(now))
   
       // create a KungleScanner
    val kungleScanner = new KungleScanner()
    logger.info("Scanner created")
    
    // scan kungles top news
    try {
    	kungleScanner.scan
    } catch {
      case ex : Exception => println(ex)
    }

    ProcessMaster ! ProcessMaster.UpdateStatus("InfoCollector")    
    logger.info("Scanner finished.")
    ActorPing.schedule(InfoCollector, InfoCollector.DoWork, scheduleIntervall minutes)
 }
  
}
