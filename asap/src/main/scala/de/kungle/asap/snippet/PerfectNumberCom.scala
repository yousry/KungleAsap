package de.kungle.asap {
package snippet {

import scala.xml.NodeSeq

import net.liftweb._
import http._
import common._
import util._
import js._
import JsCmds._
import JE._
import de.kungle.process.PerfectNumberMaster

import net.liftweb.json.JsonAST._
import net.liftweb.json.JsonDSL._


case class Crunchy(ticket: Int, start: Double, end: Double, master: Double) {
  import net.liftweb.json._
  import net.liftweb.json.JsonDSL._
  def toJson = {
    val json = ("ticket" -> ticket) ~ 
      ("start" -> start) ~ 
      ("end"-> end) ~
      ("master" -> master)
    
    compact(JsonAST.render(json))
  }
}



object PassCrunch extends Loggable{
  
  implicit val formats = net.liftweb.json.DefaultFormats
  
  def PMCall(s: String) = {
    val cResJs = net.liftweb.json.JsonParser.parse(s) 
    
   val ticket = cResJs \\ "ticket"
   val facs = cResJs \\ "factors"
    
   logger.info("Ticket received: " + ticket)
   logger.info("Factors received: " + facs)
    
    val rT  = ticket.extract[Int]

    // JField(factors,JArray(List(JInt(7), JInt(1))))	
    // TODO: 2.8 -> facs.extract[List[Double]]
    val rF = for{
      JInt(fac) <- facs
    } yield fac.extract[Int]
    
    PerfectNumberMaster ! PerfectNumberMaster.CalcDone(rT , rF.map( _.toDouble) )
    Empty
  }
  
  def unapply(in: Any): Option[String] = in match {
    case s: String => PMCall(s)
    case _ => None
  }
}

object PerfectNumberHandler extends SessionVar[JsonHandler](
  new JsonHandler {
    def apply(in: Any): JsCmd =  in match {
      	case JsonCmd("replyResult", resp, PassCrunch(s), _) => Call(resp, s)
        case _ => Noop
      }
  }
)

object PerfectNumberCom extends DispatchSnippet { 
  val dispatch = Map("render" -> buildFuncs _)
  
  def buildFuncs(in: NodeSeq): NodeSeq = {
    Script(PerfectNumberHandler.is.jsCmd &
      Function("replyResult", List("callback", "str"), PerfectNumberHandler.is.call("replyResult", JsVar("callback"),JsVar("str")))
    )
  }

  
  
  
}

}}