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
import net.liftweb.http.js.{JsCmd,JsExp}
import net.liftweb.http.SessionVar


import de.kungle.asap.model._


class SimpleWaveList extends Loggable {

  // for infinite scroll a imagetive page is calulated.
  private var pageCount = 0
  private var language = "original" // french, german

  // This method creates a ajax call to the server like a button. 
  // But instead of a button press the end of the scrollbar produces an event.
  def ajaxPageAppend(func: () => JsCmd, attrs: (String, String)*): Elem = {
    
    attrs.foldLeft(fmapFunc(contextFuncBuilder(func))(name => 
      <script type="text/javascript">{JsRaw("""$('#target').scroll(function () { 
  var containerHeight = $('#target').height();
  var scroll = $('#target').scrollTop();
  var inners = $('#innerself').height();
  
  if( containerHeight + scroll >= inners ) {
   window.setTimeout(function () { """ + {makeAjaxCall(Str(name + "=true")).toJsCmd + "; return false;"} + """ }, 500);
   window.setTimeout(function () { """ + {makeAjaxCall(Str((ajaxInvoke(() => langScrollUpdate))._1 + "=true")).toJsCmd + "; return false;"} + """ }, 510);
  };
 });""")}</script>))(_ % _)
  }

  // The server reaction by the scrollbar-end-event is to load an 
  // additional page and add it to the page. 
  def pageAppend : NodeSeq = {
    
    logger.info("PageAppend called for page: " + pageCount)
    
    def renderEntry(w: Wave) = <div class="scroll-content-item ui-widget-content"> 
      <keng><b>English: </b>{w.title_english}<br /><br /></keng> 
      <kfrn><b>French: </b>{w.title_french}<br /><br /></kfrn> 
      <kger><b>German: </b>{w.title_german}<br /><br /></kger> 
      <keng><b>English: </b>{w.summary_english}<br /></keng> 
      <kfrn><b>French: </b>{w.summary_french}<br /></kfrn> 
      <kger><b>German: </b>{w.summary_german}<br /></kger>
     </div>
    
    var nextPage = pageCount + 1
    val newDiv = "check-div" + nextPage
    pageCount = nextPage

    Wave.findAll(OrderBy(Wave.id, Descending ), StartAt(25 * nextPage), MaxRows(25)).flatMap(renderEntry) ++ <div id={newDiv}>Loading Data...</div>
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
       "smartScroll" -> ajaxPageAppend({() => SetHtml("check-div" + {pageCount}, pageAppend )})  
  )
    
  }

  def langScrollUpdate : JsCmd = {
    
    logger.info("LangScrollUpdate called for page: " + pageCount + " with language " + language)
    
    if(language == "original") JsRaw("""$('keng').show(); $('kfrn').hide(); $('kger').hide();""")
    else if(language == "french") JsRaw("""$('keng').hide(); $('kfrn').show(); $('kger').hide();""")
    else JsRaw("""$('keng').hide(); $('kfrn').hide(); $('kger').show();""")
  }  
  
  def langSel(lang: String) : JsCmd = {
    logger.info("Language selected:" + lang); 
    language = lang;
    if(language == "original") JsRaw("""$('keng').show(); $('kfrn').hide(); $('kger').hide();""")
    else if(language == "french") JsRaw("""$('keng').hide(); $('kfrn').show(); $('kger').hide();""")
    else JsRaw("""$('keng').hide(); $('kfrn').hide(); $('kger').show();""")
  }

 def languageSelect: NodeSeq = ajaxSelect(
   List(("original", "original"),
        ("french", "français"),
        ("german", "deutsch")
 ), Full(language), (opt: String) =>{logger.info(opt); langSel(opt)}) 
 
}
