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

  val sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S", java.util.Locale.US )
  
  def buildEntries(doc: xml.Elem) : List[KungleNews] = {    

    def processEntry(node: scala.xml.Node) = new KungleNews(
     (node \ "@id").text.toLong,
     (node \ "title").text,
     (node \ "summary").text,
     (node \ "publisher").text,
     sdf.parse((node \ "published").text),
     (node \ "country").text,
     (node \ "url").text,
     (node \ "topic").text,
     (node \ "originalLanguage").text
    )
    
    def processEntries(xs : List[scala.xml.Node]) = {
      xs map(processEntry) 
    }
       
    doc match {
      case <kungle_news_api><entries>{entries@ _*}</entries></kungle_news_api> => processEntries(entries.toList)
      case _ => List()
    }    
    
  }
  
    
}