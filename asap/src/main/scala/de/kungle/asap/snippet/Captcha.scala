package de.kungle.asap {
package snippet {

import _root_.scala.xml.NodeSeq
import _root_.net.liftweb.util.Helpers
import Helpers._
import model._  
import net.liftweb.mapper._
import net.liftweb.common._
  
import scala.util.Random

class Captcha {
  
  val randomGenerator = new Random
  
  def freeQuestions: Long = Captcha.count(By(Captcha.IsSolved,false))
  
  def findCaptcha: Box[model.Captcha] = Captcha.find(By(Captcha.IsSolved,false)) 

  /**
   * Wrap the captcha in a availability check
   */
  def captchaIsAvail(doc: NodeSeq) = if(freeQuestions > 0) doc else <span>Captcha Service Down</span>

  /**
   * Randomize th db query
   */
  def calcHints(c: model.Captcha): NodeSeq = {
    val randomIndexfromRemain = (for(i <- 1 to 5) yield randomGenerator.nextInt(i)) reverse
    var hints = List(c.Question, c.Alternative_A,c.Alternative_B,c.Alternative_C,c.Alternative_D) 
    val hintsRandomised = randomIndexfromRemain.map( i => {val r = hints(i);hints -= r;r}).toList 
    <span>{hintsRandomised.mkString(", ")}</span>
  }
  
  def captcha(doc: NodeSeq): NodeSeq = findCaptcha match {
    case Full(c) => bind("capgen", doc,
                         	"gapTextA" -> c.Sentence_A,
                         	"gapTextB" -> c.Sentence_B,
                         	"gapTextC" -> c.Sentence_C,
                         	"yourGuess" -> <span>yourGuess</span>,
                         	"hints" -> calcHints(c))
    case _ => <span>Error</span>

  }
}

}
}