package de.kungle.asap.model

import net.liftweb._
import net.liftweb.common._
import mapper._
import http._
import SHtml._
import util._

class User extends MegaProtoUser[User] {
  def getSingleton = User
  
  object openId extends MappedPoliteString(this, 200)
  object userName extends MappedPoliteString(this, 200)
  object originalLanguage extends MappedPoliteString(this, 200)
  
}

object User extends User with MetaMegaProtoUser[User] {
  
//   override def fieldOrder = fieldOrder ++ List(openId, userName, originalLanguage)

  override def screenWrap = Full(<lift:surround with="default" at="content">
                                   <lift:bind /></lift:surround>)


}