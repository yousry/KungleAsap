package de.kungle.asap {
package comet {

import scala.xml._

import net.liftweb.common._
import net.liftweb.http._
import net.liftweb.http.js._
import net.liftweb.http.js.JsCmds._
import net.liftweb.http.js.JE._

import net.liftweb.mapper._
import de.kungle.asap.model.PerfectNumber
import de.kungle.process.PerfectNumberMaster
import de.kungle.asap.snippet.Crunchy

class PerfectNumberStatus extends CometActor with Loggable{
  
  var donations : Double = 0
   
  override def defaultPrefix = Full("pfn")
  
  override def render = bind(
    "lastPerfect" -> <span id="lastPerfect">{lastPefect}</span>,
    "lastNumber" -> <span id="lastNumber">{lastNumber}</span>,
    "myDonation" -> <span id="myDonation">{myDonation}</span>,
    "allDonation" -> <span id="allDonation">{allDonation}</span>
  )
  
  
  def lastPefect : NodeSeq = {PerfectNumber.findAll(OrderBy(PerfectNumber.MyNumber, Descending), MaxRows(1)).head.MyNumber.is }
  def lastNumber : NodeSeq = {PerfectNumber.findAll(OrderBy(PerfectNumber.MyNumber, Descending), MaxRows(1)).head.LastCheckedNumber.is }
  def allDonation : NodeSeq = {PerfectNumber.findAll(OrderBy(PerfectNumber.MyNumber, Descending), MaxRows(1)).head.DonatedCalculations.is }
  def myDonation : NodeSeq = {donations.toString} 
    
  override def lowPriority : PartialFunction[Any, Unit] = {
    case DoCrunch(c) => {      
      donations += Math.min(c.end,c.master) - c.start      
      partialUpdate(
        SetHtml("lastPerfect", <span id="lastPefect">{lastPefect}</span>) &
        SetHtml("lastNumber", <span id="lastNumber">{lastNumber}</span>) &
        SetHtml("allDonation", <span id="allDonation">{allDonation}</span>) &
        SetHtml("myDonation", <span id="myDonation">{myDonation}</span>) &
        OnLoad(JsRaw("findFactors('"+ c.toJson +"');"))
 
      )
      
      }
    case m => logger.error("PerfectNumberStatus Unidentified Command: " + m)
  }
     
  override def localSetup {
     logger.info("PerfectNumberStatus client setup")
     PerfectNumberMaster ! new PerfectNumberMaster.SubscribePerfectNumberMaster(this)
     super.localSetup
  }
  
   override def localShutdown {
    PerfectNumberMaster ! new PerfectNumberMaster.UnSubscribePerfectNumberMaster(this)
    super.localShutdown
  }

}

case class DoCrunch(crunchy: Crunchy)

}}