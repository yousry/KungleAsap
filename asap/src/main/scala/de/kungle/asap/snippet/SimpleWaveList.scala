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
  var offset = 15
  
  if( containerHeight + scroll + 50 >= inners ) {
   window.setTimeout(function () { """ + {makeAjaxCall(Str(name + "=true")).toJsCmd + "; return false;"} + """ }, 500);
   window.setTimeout(function () { """ + {makeAjaxCall(Str((ajaxInvoke(() => langScrollUpdate))._1 + "=true")).toJsCmd + "; return false;"} + """ }, 510);
   window.setTimeout(function () { """ + {makeAjaxCall(Str((ajaxInvoke(() => draggableUpdate(Wave.findAll(OrderBy(Wave.id, Descending ), StartAt(25 * pageCount), MaxRows(25)))))._1 + "=true")).toJsCmd + "; return false;"} + """ }, 520);
  };
 });""")}</script>))(_ % _)
  }

  
        
  def renderPic(w: Wave) : NodeSeq = {
    val picID = "pic_" + w.id.is 
    <div class="span-4" id={picID} >
      <img src="/gfx/thumbnails/text3592.png" alt="PROCESS DATA" />
    </div> 
  }
        
  def renderTitle(lang: String, w: Wave)= lang match {
    case "french" => <a href={w.url.is}>{w.title_french}</a>;
    case "german" => <a href={w.url.is}>{w.title_german}</a>;
    case _ => <a href={w.url.is}>{w.title_english}</a>;
  }

  def dragImgs(ws: List[Wave]) : JsRaw = {
    def sigh(w: Wave) : String = "$(\"#pic_"+ w.id.is +"\").draggable({revert: true, helper: 'clone', appendTo: 'body', opacity: 0.4, tolerance: 'touch'});"  //  "$(\"#pic_" + w.id.is + "\").draggable({ revert: true, appendTo: 'body', opacity: 0.5});"
    val v = ws.map{ s => sigh(s)}.mkString
    JsRaw("$(function() {"  +{v}+ "});") 
  }
  
  // The server reaction by the scrollbar-end-event is to load an 
  // additional page and add it to the page. 
  def pageAppend : NodeSeq = {
    
    logger.info("PageAppend called for page: " + pageCount)
    
    def renderEntry(w: Wave) = <div class="scroll-content-item ui-widget-content">
      {renderPic(w)}
      <div class="span-22 last">
        <keng>{renderTitle("english", w)}</keng>
        <kfrn>{renderTitle("french", w)}</kfrn>
        <kger>{renderTitle("german", w)}</kger>
        <br/>
        Publisher: <b>{w.publisher}</b><br/>
        {w.publishingDate}<hr/>
      </div>
      <div class="summary span-26 last">   
        <keng>{w.summary_english}<br /></keng> 
        <kfrn>{w.summary_french}<br /></kfrn> 
        <kger>{w.summary_german}<br /></kger>
      </div>
    </div> 
    
    var nextPage = pageCount + 1
    val newDiv = "check-div" + nextPage
    pageCount = nextPage
    
    val waves = Wave.findAll(OrderBy(Wave.id, Descending ), StartAt(25 * nextPage), MaxRows(25))
    
    waves.flatMap(renderEntry) ++ 
      <div id={newDiv}>Loading Data...</div> 
  }

    def queryTabel(in: NodeSeq): NodeSeq = {

        val waves = Wave.findAll(OrderBy(Wave.id, Descending ), MaxRows(25))
        
        def renderEntry(w: Wave): NodeSeq = bind("entry", chooseTemplate("query", "entries", in),
                "titleEnglish" -> {renderTitle("english", w)},
                "titleFrench" -> {renderTitle("french", w)},
                "titleGerman" -> {renderTitle("german", w)},
                "publisher" -> w.publisher,
                "published" -> w.publishingDate,
	            "summaryEnglish" -> w.summary_english,
                "summaryFrench" -> w.summary_french,
                "summaryGerman" -> w.summary_german,
                "renderPic" -> renderPic(w)
        )
  
  val entries = waves.flatMap(renderEntry)
  
  bind("query", in, 
       "entries" -> entries,
       "smartScroll" -> ajaxPageAppend({() => SetHtml("check-div" + {pageCount}, pageAppend )}),
       "dragImgs" -> <script type="text/javascript">{dragImgs(waves)}</script> 
  )
    
  }

    
    
  def draggableUpdate(ws: List[Wave]) : JsCmd = {
    logger.info("Draggable Update called.")
    dragImgs(ws)
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
        ("french", "fran\u00E7ais"),
        ("german", "deutsch")
 ), Full(language), (lang: String) =>{logger.info("Language " + lang + "selected."); langSel(lang)}) 
 
}
