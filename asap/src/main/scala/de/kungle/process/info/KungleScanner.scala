package de.kungle.process.info

import scala.xml._
import net.liftweb.common._
import net.liftweb.util.TimeHelpers._

class KungleScanner extends Loggable {
  val url="http://www.kungle.de/Trend/api/top" 
  
  def scan() = {
    val cmd = new java.net.URL(url)
    val doc = XML.load(cmd.openStream)
    logger.info("Kungle Top News Loaded at %s".format(now))
  
    val entries = KungleNews.buildEntries(doc)
    ()
  }
  
}
