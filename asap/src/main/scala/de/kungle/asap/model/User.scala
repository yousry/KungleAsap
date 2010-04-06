package de.kungle.asap.model

import net.liftweb._
import mapper._
import http._
import SHtml._
import util._

class User extends LongKeyedMapper[User] with IdPK {
  def getSingleton = User
  
  object name extends MappedPoliteString(this, 200)
  object foreName extends MappedPoliteString(this, 200)
  object middleName extends MappedPoliteString(this, 200)

    
  object eMail extends MappedPoliteString(this, 200)

  
  object openId extends MappedPoliteString(this, 200)
  object userName extends MappedPoliteString(this, 200)

  object originalLanguage extends MappedPoliteString(this, 200)
}

object User extends User with LongKeyedMetaMapper[User] {
  override def fieldOrder = List(id,name,foreName, middleName, eMail, openId, userName)
}