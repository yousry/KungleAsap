package de.kungle.asap.model

import net.liftweb._
import net.liftweb.common._
import net.liftweb.sitemap._
import net.liftweb.sitemap.Loc._
import mapper._
import http._
import SHtml._
import util._


import _root_.net.liftweb.http._
import js._
import JsCmds._
import _root_.scala.xml.{NodeSeq, Node, Text, Elem}
import _root_.scala.xml.transform._
import _root_.net.liftweb.sitemap._
import _root_.net.liftweb.sitemap.Loc._
import _root_.net.liftweb.util.Helpers._
import _root_.net.liftweb.util._
import _root_.net.liftweb.common._
import _root_.net.liftweb.util.Mailer._
import S._
          
import de.kungle.asap.snippet._


class User extends MegaProtoUser[User] {
  def getSingleton = User

  var replaceString : String =  "user_" + (new java.util.Date).getTime
  
  this.email( replaceString + "@kungleImp.de")
  this.firstName("first_" + replaceString  )
  this.lastName("last_" + replaceString  )

  object openId extends MappedPoliteString(this, 200)
  
  object userName extends MappedPoliteString(this, 200) {
    override def displayName = "User Name"
    override def validations =  isToShort _ :: valUnique(S.??("unique.email.address")) _ :: super.validations

    def isToShort(s: String) = if(s.length < 4) List(FieldError(this, "Username to Short."))
                               else List()
    
  }
  
  object avatar extends MappedLongForeignKey(this, Avatar) {
    override def defaultValue = 1l
  }
  
}

object User extends User with MetaMegaProtoUser[User] with Loggable {

    
  def signup(doc: NodeSeq) = {
    val theUser: User = create
    val theName = signUpPath.mkString("")

    def testSignup() {
      validateSignup(theUser) match {
        case Nil =>
          actionsAfterSignup(theUser)
          val dlgs : List[DialogMeta] = dialogInfo.get
          dialogInfo(dlgs.map(x => if(x.id == "forRegisterDialog") new DialogMeta(x.id,x.position,false) else x))
          S.redirectTo(homePage)

        case xs => logger.info("Fehler: " + xs); S.error(xs); S.error("Failed Registration") ; signupFunc(Full(innerSignup _))
      }
    }

    def innerSignup = bind("user",
                           signupXhtml(theUser))

    val blub = bind("sgn", doc, 
      "inner" -> innerSignup,
	  "submit" ->SHtml.submit(S.??("sign.up"), testSignup _))
 
	<form method="post" action={S.uri}>{blub}</form>
  }
    
  override def signupXhtml(user: User): Elem = {
    (<table><tr><td colspan="2"><h4>{ S.??("sign.up") }</h4></td></tr>{localForm(user, false)}</table>)
  }

// ------------------------------------------------------------    
    
  override def validateSignup(user: User): List[FieldError] = {
    
    var error = ""
    
    sessionCaptcha.get match {
      case Full(s) => {
        logger.info("Your answer: " +s.answer + " Correct: " + s.question)
        if(s.answer.toLowerCase.trim == s.question) s.closeQuestion
        error = if(s.answer.toLowerCase.trim != s.question) "Wrong Guess." else ""
      }
      case _ => {logger.info("Captcha is missing"); error = "Captcha missing."}
    }
    
    if(error != "") FieldError(user.password, error) :: super.validateSignup(user) else super.validateSignup(user)
  }
  
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