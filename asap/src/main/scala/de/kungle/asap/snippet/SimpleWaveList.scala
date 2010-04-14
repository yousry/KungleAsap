package de.kungle.asap.snippet

import scala.xml._

import net.liftweb.common._

import _root_.scala.xml.NodeSeq
import _root_.net.liftweb.util.Helpers
import Helpers._
import _root_.net.liftweb.mapper._
import net.liftweb.http.S._
import net.liftweb.http.SHtml._
import net.liftweb.http.js.JE._
import net.liftweb.http.js.JsCmds._
import net.liftweb.http.js.JsCmd
import net.liftweb.http.SessionVar


import de.kungle.asap.model._


class SimpleWaveList extends Loggable {
  

  private var blubberCount = 0
  
  
  
  def ajaxBlubber(func: () => JsCmd, attrs: (String, String)*): Elem = {
    attrs.foldLeft(fmapFunc(contextFuncBuilder(func))(name =>
<script type="text/javascript">{JsRaw("""
$('#target').scroll(function () { 
  var containerHeight = $('#target').height();
  var scroll = $('#target').scrollTop();
  var inners = $('#innerself').height();
  var result = "ok"
  
  if( containerHeight + scroll >= inners ) {
   result = "false"; 

   window.setTimeout(function () { """ + {makeAjaxCall(Str(name + "=true")).toJsCmd + "; return false;"} + """ }, 500);

  };
  $('#alles').attr({value: containerHeight + '/' + scroll + '/' + inners +' ' + result});
 });""")}</script>))(_ % _)
  }
  
  def blubber : NodeSeq = {
    
    logger.info("Blubber Refresh called for page: " + blubberCount)
    
    def renderEntry(w: Wave) = <div class="scroll-content-item ui-widget-content"> 
      <b>English: </b>{w.title_english}<br /> 
      <b>French: </b>{w.title_french}<br /> 
      <b>German: </b>{w.title_german}<br /><br /> 
      <b>English: </b>{w.summary_english}<br /> 
      <b>French: </b>{w.summary_french}<br /> 
      <b>German: </b>{w.summary_german}<br /><br /> 
     </div>
    
    var actualBlubber = blubberCount + 1
    val newDiv = "check-div" + actualBlubber
    blubberCount = actualBlubber

    Wave.findAll(OrderBy(Wave.id, Descending ), StartAt(25 * actualBlubber), MaxRows(25)).flatMap(renderEntry) ++ <div id={newDiv}>actualBlubber</div>
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
       "smartScroll" -> ajaxBlubber({() => SetHtml("check-div" + {if(blubberCount == 0)"" else  blubberCount}, blubber )})  
  )
    
  }
}
