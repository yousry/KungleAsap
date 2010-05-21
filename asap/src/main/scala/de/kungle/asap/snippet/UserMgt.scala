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
import net.liftweb.http.js.JE._
import net.liftweb.http.js._
import JsCmds._

import de.kungle.asap.model._
import de.kungle.process.ProcessMaster

class UserMgt {
  
  def initUserDialogs : NodeSeq = {
    def initDlgs() : JsCmd = JsRaw(
"""
$('#forUserDialog').dialog({title: '""" + S.??("login") + """', width: 332, height: 167, autoOpen: false, resizable: false });
$('#forRegisterDialog').dialog({title: '""" + S.??("sign.up") + """', width: 600, height: 200, autoOpen: false, resizable: false });
""" 
    )
    Script(OnLoad(initDlgs))
  }

  def userMenu : NodeSeq = {
    def userLogin() : JsCmd = JsRaw("$('#forUserDialog').dialog('open')")
    def userLogout() : JsCmd = User.logout 
    def userRegister() : JsCmd = JsRaw("$('#forRegisterDialog').dialog('open')")
    
    if(User.loggedIn_?) <ul><li>{a(() => userLogout, Text(S.??("logout")))}</li></ul>
    else <ul><li>{a(() => userLogin, Text(S.??("login")))}</li><li>{a(() => userRegister, Text(S.??("sign.up")))}</li></ul>
    
    
  }

  def loginStatus = {
    
    if(User.loggedIn_?) {
      val yourName = User.currentUser match {
        case Full(u) => u.userName.is
        case _ => "error"
      }
      <div>
      <h5>You are: {yourName}</h5>
      <p>{Avatar.findAll(By(Avatar.lookup, "1")).head.imageUrl}</p>
      </div>
    } else {
      <div>
      <h5>You are: anonymous</h5>
      <p>{Avatar.findAll(By(Avatar.lookup, "2")).head.imageUrl}</p>
      </div>
    }
  } 

  
  def signUpDialog : NodeSeq = {
    User.signup
  }
  
  
  def loginDialog : NodeSeq = {
            
    var username = ""
    var pwd = ""
    
    def testPwd {
      User.find(By(User.userName, username)).filter(_.password.match_?(pwd)).map{
        u => {logUserIn(u); println("LOGIN CALLED")}
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
