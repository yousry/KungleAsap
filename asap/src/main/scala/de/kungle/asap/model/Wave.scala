package de.kungle.asap.model

import net.liftweb._
import mapper._
import http._
import SHtml._
import util._

class Wave extends LongKeyedMapper[Wave] with IdPK {
  def getSingleton = Wave
  
  object title_english extends MappedPoliteString(this, 200)
  object title_french extends MappedPoliteString(this, 200)
  object title_german extends MappedPoliteString(this, 200)

  object summary_english extends MappedPoliteString(this, 200)
  object summary_french extends MappedPoliteString(this, 200)
  object summary_german extends MappedPoliteString(this, 200)

  object publisher extends MappedPoliteString(this, 200)
  object publishingDate extends MappedDateTime(this)

  object geoCode extends MappedPoliteString(this, 2)
  object url extends MappedPoliteString(this, 200)
  object topic extends MappedPoliteString(this, 200)
  object originalLanguage extends MappedPoliteString(this, 200)

  object kungleId extends MappedLong(this) 
  object markedBanned extends MappedInt(this)
  
}

object Wave extends Wave with LongKeyedMetaMapper[Wave] {
  override def fieldOrder = List(id, 
  title_english,title_french,title_german,
  summary_english, summary_french, summary_german,
  publisher, publishingDate,
  geoCode,url,topic,originalLanguage,
  kungleId, 
  markedBanned)
}