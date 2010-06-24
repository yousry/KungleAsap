package de.kungle.asap {
package snippet {

  import _root_.net.liftweb.http._
import _root_.scala.xml.NodeSeq
import _root_.net.liftweb.util.Helpers
import Helpers._
import model._  
import net.liftweb.mapper._
import net.liftweb.common._

import net.liftweb.http.js.JE._
import net.liftweb.http.js.JsCmds._
import net.liftweb.http.js.{JsCmd,JsExp}

import net.liftweb.http.jquery.JqSHtml
import net.liftweb.http.js.jquery.JqJsCmds
import net.liftweb.http.js.jquery.JqJsCmds._

import net.liftweb.http.SessionVar

import _root_.scala.xml._

import scala.util.Random

object sessionCaptcha extends SessionVar[Box[Captcha]](Empty)

class Captcha {
  
  sessionCaptcha(Full(this))
  
  var question: String = ""
  var answer: String = "Type Here"

  val randomGenerator = new Random
  
  def freeQuestions: Long = Captcha.count(By(Captcha.IsSolved,false))
  
  def findCaptcha: Box[model.Captcha] = Captcha.find(By(Captcha.IsSolved,false)) 

  var activeCaptcha: Box[model.Captcha] = Empty
  
  def closeQuestion = activeCaptcha match {
    case Full(c) => c.IsSolved(true); c.save
    case _ => ()                                  
  }
  
  
  /**
   * Wrap the captcha in a availability check
   */
  def captchaIsAvail(doc: NodeSeq) = if(freeQuestions > 0) doc else <span>Captcha Service Down</span>

  /**
   * Randomize th db query
   */
  def calcHints(c: model.Captcha): String = {
    val randomIndexfromRemain = (for(i <- 1 to 5) yield randomGenerator.nextInt(i)) reverse
    var hints = List(c.Question, c.Alternative_A,c.Alternative_B,c.Alternative_C,c.Alternative_D) 
    val hintsRandomised = randomIndexfromRemain.map( i => {val r = hints(i);hints -= r;r}).toList 
    hintsRandomised.mkString(", ")
  }
  
  def captcha(doc: NodeSeq): NodeSeq = findCaptcha match {
    case Full(c) => bind("capgen", doc,
                         "triggerCounter" -> {
                                def alertCaptcha() : JsCmd = JsRaw("$('#hints').html('" + Str(calcHints(c)) + "')")
                                val bubu = SHtml.ajaxInvoke(() => alertCaptcha)
                                val mumu = "function triggerCounter(timespan) { window.setTimeout(function () { " + SHtml.makeAjaxCall(Str((bubu)._1 + "=true")).toJsCmd + "; return false; }, 10000);}"
                                Script(JsRaw(mumu))
                            }, 
                            "counter" -> {
                                def alertCaptcha() : JsCmd = JsRaw("$('#hints').html('" + Str(calcHints(c)) + "')")
                                val bubu = SHtml.ajaxInvoke(() => alertCaptcha)
                                val mumu = "window.setTimeout(function () { " + SHtml.makeAjaxCall(Str((bubu)._1 + "=true")).toJsCmd + "; return false; }, 10000);"
                                Script(JsRaw(mumu))
                            }, 
                         	"gapTextA" -> c.Sentence_A,
                         	"gapTextB" -> c.Sentence_B,
                         	"gapTextC" -> c.Sentence_C,
                         	"yourGuess" -> { activeCaptcha = Full(c); question = c.Question.get; SHtml.text(answer, answer = _, "maxlength" -> "40") % ("onclick" -> "$(this).val('')")},
                         	"hints" -> NodeSeq.fromSeq{
                              <span id="hints" >Wait 10 Seconds</span> 
                              <noscript>Please enable JavaScript</noscript>
                         	}
    )
    case _ => <span>Error</span>

  }
}

}
}