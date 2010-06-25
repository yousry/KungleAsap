package de.kungle.asap {
package snippet {

import _root_.scala.xml.NodeSeq
import _root_.net.liftweb.util.Helpers
import Helpers._
import model._

import net.liftweb.http.js.JE._
import net.liftweb.http.js.JsCmds._
import net.liftweb.http.js.{JsCmd,JsExp}
  
import net.liftweb.http.SessionVar

object UseBackgroundEffect extends SessionVar[Boolean](true)

class NiceBackground {

  val myEffect : JsCmd = JsRaw("$(\"#myBackground\").show(\"scale\",{},2000);")
  
  val effect = if(UseBackgroundEffect.get == true) {
    UseBackgroundEffect(false)
    Script(OnLoad(myEffect)) 
  } else <span/>
  
  def background(document: NodeSeq): NodeSeq = 
    Helpers.bind("bg", document, 
                 "bgImage" -> <div id="myBackground" style="margin-left: -24px; position: absolute; top:140px; width:100%; height: 90%;z-index: 1;background:url(/photo/ExampleK);background-repeat:repeat-x;"></div>, 
                  "bgEffect" -> effect
    )
  
}


}}