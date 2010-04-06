package de.kungle.asap.model

import net.liftweb._
import mapper._
import http._
import SHtml._
import util._

class UserLog extends LongKeyedMapper[UserLog] with IdPK {
  def getSingleton = UserLog
  
  object user extends MappedLongForeignKey(this, User)
  object action extends MappedPoliteString(this, 200)

}

object UserLog extends UserLog with LongKeyedMetaMapper[UserLog] {
  
}