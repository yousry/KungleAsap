package de.kungle.asap.comet

import scala.xml._

import net.liftweb.common._
import net.liftweb.http._
import net.liftweb.http.js._
import net.liftweb.http.js.JsCmds._

import de.kungle.asap.snippet.WorkbenchNews
import de.kungle.fingerprint._

import de.kungle.asap.model._
import net.liftweb.mapper._

object TopologyStatusObj extends SessionVar[Box[TopologyStatus]](Empty)


class TopologyStatus extends CometActor with Loggable {

  override def defaultPrefix = Full("topology")
  
    // Everyone in this session should know me
  TopologyStatusObj(Full(this))
  
    override def render = bind(
      "related" -> rndRelated
    )
  
    
    
    def plotRelated = {
      val news = WorkbenchNews.get
      
      
      def calcRelated = {
        val selectedWaveIds = news.map(x => x.drop(4).toLong)
        val selectedNews = Wave.findAll(ByList(Wave.id, selectedWaveIds))
        val newsTitles = selectedNews.map(x => x.title_english.get)

        val searchResult =  new FingerPrintSearch(newsTitles)
        val resultWaves = Wave.findAll(ByList(Wave.id, searchResult.neighbours)).
          reverse.
          filter(w => !selectedWaveIds.contains(w.id) )
       
       <span>
       <h5>Selected</h5>
         <ul>{selectedNews.map(w => <li><a href={w.url.is} target="_blank">{w.title_english}</a>({w.publisher})</li>)}</ul>
       <h5>Recommendations</h5>
       <ul>{resultWaves.map(w => <li><a href={w.url.is} target="_blank">{w.title_english}</a>({w.publisher})</li>)}</ul>
       </span>  
      }

      
      if(news.isEmpty)
        Text("There are no Documents in your box.")
      else 
        calcRelated
    }
    
    def rndRelated = <span id="related">{plotRelated}</span>
       
    override def lowPriority : PartialFunction[Any, Unit] = {
     case TopologyUpdate() =>  
       partialUpdate(SetHtml("related", <span id="related">{plotRelated}</span>))
     case _ => logger.info("[TopologyStatus] Unknown Command")
    }

    
}

case class TopologyUpdate()