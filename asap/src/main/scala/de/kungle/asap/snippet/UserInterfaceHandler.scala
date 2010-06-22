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

import net.liftweb.json.JsonAST._
import net.liftweb.json.JsonDSL._

import net.liftweb.http.SessionVar

case class DialogMeta(id: String, position: (Int,Int), visible: Boolean) 

object dialogInfo extends SessionVar[List[DialogMeta]](Nil)

class UserInterfaceHandler extends Loggable{
  
  object uiHandler extends JsonHandler {
    
    implicit val formats = net.liftweb.json.DefaultFormats

    def apply(doc: Any): JsCmd = {
      
      val reply = doc match {
        case JsonCmd(cmd, _, _, _) => cmd
        case _ => """{"owner":"none","action":"none","pos":[0,0]}"""
      }

      val cResJs = net.liftweb.json.JsonParser.parse(reply) 
      val owner: String = (cResJs \\ "owner").extract[String]
      val action = (cResJs \\ "action").extract[String]
      val poss = {
        val mau = for{JInt(pos) <- cResJs \\ "pos"} yield pos.extract[Int]
        if(mau.length != 2) List(100, 100) else mau
        }

      val visibility = if(action == "close") false else true
      
      val dlgs : List[DialogMeta] = dialogInfo.get
      
      dialogInfo(new DialogMeta(owner, (poss(0), poss(1)), visibility) :: dlgs.filter(x => x.id != owner))
      
      logger.info("Active Dialogs: " + dialogInfo.get.mkString(", "))
      
      //SetHtml("uiaction", Text("(owner: "+owner+", action: "+action+", pos: "+ poss +")")) 
      JsRaw("")
      
    }
  }
  
  val clientToServer: JsCmd = Function("preCall",List("what"),uiHandler.call(JsVar("what")) )
  
  val update: JsCmd = {

        
    val defaultOpen = List("newstopology")

    val dlgsOpen = (dialogInfo.get).filter(_.visible)  
    val dlgsClosed = (dialogInfo.get).filter(x => !x.visible && defaultOpen.contains(x.id))  
    val dlgsToOpen = (dialogInfo.get).filter(x => x.visible && !defaultOpen.contains(x.id) )
    
    val toJsOpen = (selector: String) => "$('#" + selector  + "').dialog('open'); "
    val toJsClose = (selector: String) => "$(\"#" + selector  + "\").dialog('close'); "
    val toJsUpdate = (selector: String, x: Int, y: Int) => 
      "$(\"#" + selector  + "\").dialog( \"option\", \"position\", ["+ x +" ,"+ y +"] );"
    
    JsRaw( 
    	   (dlgsToOpen.map(x => toJsOpen(x.id))).mkString(" ") +
           (dlgsOpen.map(x => toJsUpdate(x.id, x.position._1, x.position._2))).mkString(" ") +
           (dlgsClosed.map(x => toJsClose(x.id))).mkString(" ")
         ) 
  }
  
  def uiBridge(doc: NodeSeq): NodeSeq = bind("inter", doc,
    "handler" -> Script(uiHandler.jsCmd),
    "caller" -> Script(clientToServer),
    "update" -> Script(OnLoad(update))
  )
  
}

}}}
