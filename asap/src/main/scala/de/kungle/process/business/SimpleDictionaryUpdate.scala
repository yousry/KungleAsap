package de.kungle.process.business

import tools.com.google.api.GoogleAPI;
import tools.com.google.api.translate.Language
import tools.com.google.api.translate.Translate

import net.liftweb.actor._
import _root_.net.liftweb._
import http._
import util._
import Helpers._
import net.liftweb.common._
import de.kungle.process.info.KungleScanner
import _root_.net.liftweb.mapper._
import de.kungle.asap.model._
import scala.collection.mutable.HashMap

object SimpleDictionaryUpdate extends LiftActor with BackgroundTask with Loggable  {
  
  // Say Hello to Google
 GoogleAPI.setHttpReferrer("http://kungleImp.de/");
  
  override var scheduleIntervall : Long = 5 // 10 Minutes intervall
  
  val segmentSize = 10
  
   protected def messageHandler = {
      case DoWork => work
      case m => logger.error("Scheduler Unidentified Command: " + m)
    }
   
  def work() = {
        
    ProcessMaster ! ProcessMaster.UpdateMessage("[SimpleDictionaryUpdate] Dictionary Updated started.")
    
    var dictionarySegment = new HashMap[String, Long]()

    def learnWords(w: Wave) = {
      val combinedText = ("\\W".r).
        replaceAllIn((w.title_english + " " + w.summary_english).toLowerCase, " ").
        split(' ').map( x => x.trim)
              
      val reducedMore = combinedText.filter(x => if(x.length < 2) false else if(x.matches(".*[0-9].*")) false else true)
      reducedMore.foreach(x =>dictionarySegment += (x -> ( (dictionarySegment.get(x) getOrElse 0l) + 1l ) ))
    }
    
    val waves = Wave.findAll(By(Wave.DictionaryIndexed, false), MaxRows(segmentSize)) 

    waves.foreach(learnWords)
    
    dictionarySegment.toList.foreach( e => {
      DictionaryEntry.find(By(DictionaryEntry.englishTerm, e._1)) match {
        case Full(w) => {w.occurrences(w.occurrences.is + e._2); w.save  }
        case Empty => {
          val dicEntry = DictionaryEntry.create
          dicEntry.frenchTerm("")
          dicEntry.englishTerm(e._1)
          dicEntry.germanTerm("")
          dicEntry.occurrences(e._2)
          dicEntry.save
        }
        case _ => () // Something Buggy
      }
    }
    )
    
    waves.foreach(w => {w.DictionaryIndexed(true); w.save})
    
    ProcessMaster ! ProcessMaster.UpdateMessage("[SimpleDictionaryUpdate] Dictionary Updated to: " + DictionaryEntry.count + " entries.")
    ProcessMaster ! ProcessMaster.UpdateStatus("SimpleDictionaryUpdate")    
    logger.info("SimpleDictionaryUpdate finished.")
    ActorPing.schedule(SimpleDictionaryUpdate, SimpleDictionaryUpdate.DoWork, scheduleIntervall minutes)
    
  }
}
