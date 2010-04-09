package de.kungle.process.business

import net.liftweb.actor._
import _root_.net.liftweb._
import http._
import util._
import Helpers._
import net.liftweb.common._
import net.liftweb.mapper._
import de.kungle.asap.model.Wave

object TranslationCollector extends LiftActor with BackgroundTask with Loggable {
  
   override var scheduleIntervall : Long = 2 // 10 Minutes intervall
       
   protected def messageHandler = {
      case DoWork => work
      case m => logger.error("Scheduler Unidentified Command: " + m)
    }

    def work() = {
      logger.info("TranslatorCollector Started at: %s".format(now))
   
      val untranslatedWaves = Wave.findAll(By(Wave.translator,"none"))
      logger.info("Waves to translate: " + untranslatedWaves.length)

      untranslatedWaves.foreach(new WaveTranslator(_))
   
      logger.info("Translator finished.")

          
      ProcessMaster ! ProcessMaster.UpdateStatus("TranslationCollector")    
      
      val stillWork = if(Wave.findAll(By(Wave.translator,"none")).length > 0) true else false
      if(stillWork) {
        logger.info("Translator: still work to be done - continue in 10 seconds.")
        ActorPing.schedule(TranslationCollector, TranslationCollector.DoWork, 10 seconds)
      } else {
        ActorPing.schedule(TranslationCollector, TranslationCollector.DoWork, scheduleIntervall minutes)
      }
 }

  
  
}
