package de.kungle.process.info

import scala.xml._
import net.liftweb.common._
import net.liftweb.util.TimeHelpers._
import de.kungle.asap.model._
import net.liftweb.mapper._

class KungleScanner extends Loggable {
  val url="http://www.kungle.de/Trend/api/top" 
  
  def scan() = {
    val cmd = new java.net.URL(url)
    val doc = XML.load(cmd.openStream)
    logger.info("Kungle Top News Loaded at %s".format(now))
  
    val entries = KungleNews.buildEntries(doc)
    logger.info("Parsed " + entries.length + " entries.")
    
    for(e <- entries) {
      Wave.find(By(Wave.orignId, e.KungleId)) match {
        case Empty => {println("New Kungle Entry")
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
        case _ => println("Old Kungle Entry")
      }
    }

    ()
  }
  
}
