package de.kungle.asap.snippet

import scala.xml._

import net.liftweb.common._

import _root_.scala.xml._
import _root_.net.liftweb.http._
import _root_.net.liftweb.http.S._
import _root_.net.liftweb.http.SHtml._
import _root_.net.liftweb.mapper._
import _root_.net.liftweb.util.Helpers._
import _root_.net.liftweb.util._
import _root_.net.liftweb.common._
import _root_.de.kungle.asap.model._
import _root_.de.kungle.asap.model.User._


import de.kungle.asap.model._
import de.kungle.process.ProcessMaster

class UserMgt {

  def logged_in_? = S.get("user_name").isDefined

  def loginStatus = {
    if(logged_in_?) {
      val yourName = S.get("user_name") getOrElse "error"
      <div>
      <h5>You are: {yourName}</h5>
      <p>{Avatar.findAll(By(Avatar.lookup, "1")).head.imageUrl}</p>
      </div>
    } else {
      <div>
      <h5>You are: anonym</h5>
      <p>{Avatar.findAll(By(Avatar.lookup, "2")).head.imageUrl}</p>
      </div>
    }
  } 
  
  def loginDialog : NodeSeq = {
            
    var username = ""
    var pwd = ""

            
    def testPwd {
      User.find(By(User.userName, username)).filter(_.password.match_?(pwd)).map{
        u => S.set("user_name", u.userName)
        S.redirectTo("/")
      }.openOr(S.error("Invalid Username/Password"))
    }

    
    <form method="post" action={S.uri}>
      <table>
       <tr><td>User Name:</td><td>{text("", u => username = u)}</td></tr>
       <tr><td>Password:</td><td>{SHtml.password("", p => pwd = p)}</td></tr>
       <tr><td>{submit("login", testPwd _)}</td></tr>
      </table>
     </form>
  }
  
  
}