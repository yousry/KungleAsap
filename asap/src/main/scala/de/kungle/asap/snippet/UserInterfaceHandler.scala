package de.kungle {
package asap {
package snippet {
  
import _root_.net.liftweb.http._
import S._
import js._
import JsCmds._
import JE._
import _root_.net.liftweb.common._
import _root_.net.liftweb.util._
import Helpers._
import _root_.scala.xml._


import net.liftweb.http.jquery.JqSHtml
import net.liftweb.http.RequestVar
import net.liftweb.http.S._
import net.liftweb.http.SHtml._
import net.liftweb.http.js.JE._
import net.liftweb.http.js.JsCmds._
import net.liftweb.http.js.{JsCmd,JsExp}

import net.liftweb.http.SessionVar

case class DialogMeta(id: String, position: (Int,Int), visible: Boolean) 

object dialogInfo extends SessionVar[List[DialogMeta]](Nil)

class UserInterfaceHandler extends Loggable{
  
  object uiHandler extends JsonHandler {
    def apply(doc: Any): JsCmd = {
      logger.info("BUBU NANA LOLO: " + doc.toString)
      JsRaw("alert('uiHandler: "+ doc +"');") 
    }
  }
  
  val clientToServer : JsCmd = Function("preCall",List("what"),uiHandler.call(JsVar("what")) )
  
  def uiBridge(doc: NodeSeq): NodeSeq = bind("inter", doc,
    "handler" -> Script(uiHandler.jsCmd),
    "caller" -> Script(clientToServer)
  )
  
}

}}}
