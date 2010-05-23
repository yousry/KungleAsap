package de.kungle.asap.model

import net.liftweb._
import mapper._
import http._
import SHtml._
import util._

class Comment extends LongKeyedMapper[Comment] with IdPK {
  def getSingleton = Comment

  object author extends MappedLongForeignKey(this, User)
  object summary_english extends MappedPoliteString(this, 200)
  object summary_french extends MappedPoliteString(this, 200)
  object summary_german extends MappedPoliteString(this, 200)

  object markedBanned extends MappedInt(this)
  object originalLanguage extends MappedPoliteString(this, 30)
  
}

object Comment extends Comment with LongKeyedMetaMapper[Comment] {
  
}