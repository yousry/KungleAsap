package de.kungle.asap.model

import net.liftweb._
import mapper._
import http._
import SHtml._
import util._

class DictionaryEntry extends LongKeyedMapper[DictionaryEntry] with IdPK {
  def getSingleton = DictionaryEntry
  
  object englishTerm extends MappedPoliteString(this, 100)
  object frenchTerm extends MappedPoliteString(this, 100)
  object germanTerm extends MappedPoliteString(this, 100)
  
  object occurrences extends MappedLong(this)
  
}

object DictionaryEntry extends DictionaryEntry with LongKeyedMetaMapper[DictionaryEntry]{
  override def fieldOrder = List(id, englishTerm, frenchTerm, germanTerm, occurrences)
}