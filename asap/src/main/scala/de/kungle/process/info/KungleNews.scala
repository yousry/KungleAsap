package de.kungle.process.info

import scala.xml._

case class KungleNews(val KungleId: Long, 
                      val title: String, 
                      val summary: String,
					  val publisher: String,
                      val published: java.util.Date,
                      val country: String,
                      val url: String,
                      val topic: String,
                      val originalLanguage: String) {

}

object KungleNews {
  
  def buildEntries(doc: xml.Elem) : List[KungleNews] = {
    
    def processEntries(xs : List[scala.xml.Node]) = {
      xs map( x => 0) 
    }
       
    doc match {
      case <kungle_news_api><entries>{entries@ _*}</entries></kungle_news_api> => processEntries(entries.toList)
      case _ => ()
    }    
    
    List()
  }
  
    
}