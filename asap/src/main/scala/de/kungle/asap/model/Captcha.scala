package de.kungle.asap.model

import net.liftweb._
import mapper._
import http._
import SHtml._
import util._

class Captcha extends LongKeyedMapper[Captcha] with IdPK {
  def getSingleton = Captcha

  object Sentence_A extends MappedPoliteString(this, 100) {
    override def defaultValue = ""
  }
  
  object Sentence_B extends MappedPoliteString(this, 100) {
    override def defaultValue = ""
  }
  object Sentence_C extends MappedPoliteString(this, 100) {
    override def defaultValue = ""
  }
  
  object IsSolved extends MappedBoolean(this) {
    override def defaultValue = false
  }
  
}

object Captcha extends Captcha with LongKeyedMetaMapper[Captcha] {
  override def fieldOrder = List(id, Sentence_A, Sentence_B, Sentence_C, IsSolved)
}
