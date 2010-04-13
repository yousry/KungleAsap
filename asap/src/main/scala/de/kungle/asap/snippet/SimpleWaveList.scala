package de.kungle.asap.snippet

import scala.xml._

import net.liftweb.common._

import _root_.scala.xml.NodeSeq
import _root_.net.liftweb.util.Helpers
import Helpers._
import _root_.net.liftweb.mapper._
import net.liftweb.http.SHtml._
import net.liftweb.http.js.JE._
import net.liftweb.http.js.JsCmds._

import de.kungle.asap.model._


class SimpleWaveList extends Loggable {

  def blubber : NodeSeq = {
    
    def renderEntry(w: Wave) = <div class="scroll-content-item ui-widget-content"> 
      <b>English: </b>{w.title_english}<br /> 
      <b>French: </b>{w.title_french}<br /> 
      <b>German: </b>{w.title_german}<br /><br /> 
      <b>English: </b>{w.summary_english}<br /> 
      <b>French: </b>{w.summary_french}<br /> 
      <b>German: </b>{w.summary_french}<br /><br /> 
     </div>
    
     Wave.findAll(OrderBy(Wave.id, Descending ), MaxRows(25)).flatMap(renderEntry)
    
  }
  
  def queryReload = {
    ajaxButton(Text("Press Me"),{() => 
      logger.info("Press Me Pressed")
      SetHtml("check-div", blubber )})
  }

    def queryTabel(in: NodeSeq): NodeSeq = {
      
        def renderEntry(w: Wave): NodeSeq = bind("entry", chooseTemplate("query", "entries", in),
                "titleEnglish" -> w.title_english,
                "titleFrench" -> w.title_french,
                "titleGerman" -> w.title_german,
	            "summaryEnglish" -> w.summary_english,
                "summaryFrench" -> w.summary_french,
                "summaryGerman" -> w.summary_german
        )
  
  val entries = Wave.findAll(OrderBy(Wave.id, Descending ), MaxRows(25)).flatMap(renderEntry)
      
  bind("query", in, 
       "entries" -> entries,
       "button" -> queryReload)
      
    }
}
