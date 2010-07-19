package de.kungle.asap.model

import net.liftweb._
import mapper._
import http._
import SHtml._
import util._
import net.liftweb.util.Helpers
import scala.xml._

class PhotoRef extends LongKeyedMapper[PhotoRef] with IdPK {
  def getSingleton = PhotoRef

  object Title extends MappedPoliteString(this, 20) 
  object Topic extends MappedPoliteString(this, 20) 
  object TopicNumber extends MappedInt(this)
  object PublishingDate extends MappedDate(this)
  object Publisher extends MappedPoliteString(this, 100) {override def defaultValue = ""}
  object ArticleUrl extends MappedPoliteString(this, 250) {override def defaultValue = ""}
  object PhotoFile extends MappedLongForeignKey(this, Photo)
}

object PhotoRef extends PhotoRef with LongKeyedMetaMapper[PhotoRef] {
  override def fieldOrder = List(id, Title, Topic, TopicNumber, PublishingDate, Publisher, ArticleUrl, PhotoFile)
}