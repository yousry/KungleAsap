package de.kungle.asap.snippet

import net.liftweb._
import http._
import common._
import util._
import js._
import JsCmds._
import JE._

import de.kungle.process.StrokeMaster

import scala.xml.NodeSeq

object myStrokeId extends SessionVar[Box[Long]](Empty)


object XNews {
  def unapply(in: Any): Option[String] = in match {
    case s: String => {
      val ents = WorkbenchNews.get
      if( ents.contains(s) )
        Some("News already Selected")
      else {
        WorkbenchNews(s :: ents) 
        Some("%s News in your Box".format(ents.length + 1) )
      }
    }
                       
    case _ => None
  }
}

object XStroke extends Loggable{
  def unapply(in: Any): Option[String] = in match {
    case s: String => {
      myStrokeId.get match {
        case Full(x) => StrokeMaster ! StrokeMaster.StrokeUpdate(x, s)
        case _ => ()
      }
      Empty
    }
                       
    case _ => None
  }
}

object WorkbenchNews extends SessionVar[List[String]](List())

object WaveJasonHandler extends SessionVar[JsonHandler](
  new JsonHandler {
    def apply(in: Any): JsCmd =  in match {
        case JsonCmd("addNews", resp, XNews(s), _) => Call(resp, s)
        case JsonCmd("addStroke", resp, XStroke(s), _) => Call(resp, s)
        case myCmd : String => Call("resp", "s")
        case _ => Noop
      }
  }
)

object WaveJason extends DispatchSnippet{

  val dispatch = Map("render" -> buildFuncs _)

  def buildFuncs(in: NodeSeq): NodeSeq = {
    Script(WaveJasonHandler.is.jsCmd &
      Function("addNews", List("callback", "str"), WaveJasonHandler.is.call("addNews", JsVar("callback"),JsVar("str"))) &
      Function("addStroke", List("callback", "str"), WaveJasonHandler.is.call("addStroke", JsVar("callback"),JsVar("str")))
    )
  }

}
