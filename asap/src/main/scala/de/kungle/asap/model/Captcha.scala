package de.kungle.asap.model

import net.liftweb._
import mapper._
import http._
import SHtml._
import util._

class Captcha extends LongKeyedMapper[Captcha] with IdPK {
  def getSingleton = Captcha

    
  object Question extends MappedPoliteString(this, 60) {
    override def defaultValue = ""
  }
  
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
    
  object Alternative_A extends MappedPoliteString(this, 60) {
    override def defaultValue = ""
  }

  object Alternative_B extends MappedPoliteString(this, 60) {
    override def defaultValue = ""
  }
   
  object Alternative_C extends MappedPoliteString(this, 60) {
    override def defaultValue = ""
  }
      
  object Alternative_D extends MappedPoliteString(this, 60) {
    override def defaultValue = ""
  }
  
}

object Captcha extends Captcha with LongKeyedMetaMapper[Captcha] {
  override def fieldOrder = List(id, Question, Sentence_A, Sentence_B, Sentence_C, IsSolved, Alternative_A, Alternative_B, Alternative_C, Alternative_D)
}
