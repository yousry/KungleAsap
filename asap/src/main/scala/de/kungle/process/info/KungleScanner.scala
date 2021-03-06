package de.kungle.process.info

import scala.xml._
import net.liftweb.common._
import net.liftweb.util.TimeHelpers._
import de.kungle.asap.model._
import net.liftweb.mapper._

class KungleScanner extends Loggable {
  val url="http://www.kungle.de/api/top" 
  
  def createWave(e: KungleNews) : Unit = {
    val w = Wave.create
          w.title_english(e.title)
          w.summary_english(e.summary)
          w.publisher(e.publisher)
          w.publishingDate(e.published)
          w.geoCode(e.country)
          w.url(e.url)
          w.topic(e.topic)
          w.originalLanguage(e.originalLanguage)
          w.orignId(e.KungleId)
          w.save
  }
  
  def checkNewsAge(age: java.util.Date) : Boolean = {
    val actualM = (new java.util.Date).getTime
    val ageM = age.getTime
    val timeSpan = actualM - ageM
    val timeLimit = (1000 * 60) * 60 * 5
    if(timeSpan > timeLimit) false else true
  }
  
  def checkForWave(e: KungleNews) : Boolean =  Wave.find(By(Wave.orignId, e.KungleId)) match {
    case Empty => if(checkNewsAge(e.published)){createWave(e); true} else false
    case _ => false
  }
  
  def scan() = {
    val cmd = new java.net.URL(url)
    val doc = XML.load(cmd.openStream)
    logger.info("Kungle Top News Loaded at: %s".format(now))
  
    val entries = KungleNews.buildEntries(doc)
    logger.info("Parsed " + entries.length + " entries.")

    val newWaves  = entries.map(checkForWave).filter(_ == true).length
    logger.info("Scan creates " + newWaves + " entries")
  }
}
