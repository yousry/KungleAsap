package de.kungle.process.info

import scala.xml._

class KungleScanner {
  val url="http://www.kungle.de/Trend/api/top" 
  
  def scan() = {
    
    
    val cmd = new java.net.URL(url)
    val kungleXML = XML.load(cmd.openStream)

    
    ()
  }
  
}
