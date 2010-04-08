package de.kungle.asap.snippet

import _root_.scala.xml.NodeSeq
import _root_.net.liftweb.util.Helpers
import Helpers._
import _root_.net.liftweb.mapper._
import de.kungle.asap.model._


class SimpleWaveList {


    def queryTabel(in: NodeSeq): NodeSeq = {

      
      
/*
   <entry:titleEnglish/><hl/>
   <entry:titleFrench/><hl/>
   <entry:titleGerman/><hl/>
   <h1>Summaries</h1>
   <entry:summaryEnglish/><hl/>
   <entry:summaryFrench/><hl/>
   <entry:summaryGerman/><hl/>

 
*/
      
        def renderEntry(w: Wave): NodeSeq = bind("entry", chooseTemplate("query", "entries", in),
                "titleEnglish" -> w.title_english,
                "titleFrench" -> w.title_french,
                "titleGerman" -> w.title_german,
	            "summaryEnglish" -> w.summary_english,
                "summaryFrench" -> w.summary_french,
                "summaryGerman" -> w.summary_german
        )
  
  val entries = Wave.findAll(OrderBy(Wave.publishingDate, Descending ), MaxRows(3)).flatMap(renderEntry)
      
      bind("query", in, "entries" -> entries)
      
    }


  
}
