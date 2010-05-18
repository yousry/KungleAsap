package de.kungle.asap.model

import net.liftweb._
import net.liftweb.common._
import net.liftweb.sitemap._
import net.liftweb.sitemap.Loc._
import mapper._
import http._
import SHtml._
import util._

class User extends MegaProtoUser[User] {
  def getSingleton = User

  var replaceString : String =  "user_" + (new java.util.Date).getTime
  
  this.email( replaceString + "@kungleImp.de")
  this.firstName("first_" + replaceString  )
  this.lastName("last_" + replaceString  )

  object openId extends MappedPoliteString(this, 200)
  object userName extends MappedPoliteString(this, 200)
  object avatar extends MappedLongForeignKey(this, Avatar) {
    override def defaultValue = 1l
  }
  
}

object User extends User with MetaMegaProtoUser[User] {

  override def signupFields  = userName :: password :: Nil
  
  override def resetPasswordMenuLocParams = LocGroup("user") :: super.resetPasswordMenuLocParams
  override def loginMenuLocParams = LocGroup("user") :: super.loginMenuLocParams
  override def createUserMenuLocParams = LocGroup("user") :: super.createUserMenuLocParams
  override def lostPasswordMenuLocParams = LocGroup("user") :: super.lostPasswordMenuLocParams
  override def logoutMenuLocParams = LocGroup("user") :: super.loginMenuLocParams
  
  override def fieldOrder = userName :: password :: avatar :: Nil

  override def skipEmailValidation = true
  
  override def screenWrap = Full(<lift:surround with="default" at="content"><lift:bind /></lift:surround>)

}