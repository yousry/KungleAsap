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
import de.kungle.asap.comet.WhatHappensNextInit


class UserMgt extends Loggable {
  
  
  val actUIManagement = (myId: String) => """,
	                           close: function(event, ui) {
		                           var position = $(this).dialog( "option", "position" );
		                           
		                           var jsonTransmitter = {
	        					   		"owner": '""" + myId +  """',
	        					   		"action": "close",
	        							"pos": position 
	    						   };
		                           
		                           preCall(JSON.stringify(jsonTransmitter)); },
	                           
	                           dragStop: function(event, ui) {
	                           var position = $(this).dialog( "option", "position" );
	                           
	                           var jsonTransmitter = {
        					   		"owner": '""" + myId +  """',
        					   		"action": "dragStop",
        							"pos": position 
    						   };
	                           
	                           preCall(JSON.stringify(jsonTransmitter)); },

	                           open: function(event, ui) {
	                           var position = $(this).dialog( "option", "position" );
	                           
	                           var jsonTransmitter = {
        					   		"owner": '""" + myId +  """',
        					   		"action": "dragStop",
        							"pos": position 
    						   };
	                           
	                           preCall(JSON.stringify(jsonTransmitter)); }
"""
  
  def initUserDialogs : NodeSeq = {
    def initDlgs() : JsCmd = JsRaw(
"""
$('#forUserDialog').dialog({title: '""" + S.??("login") + """', width: 332, height: 167, autoOpen: false, resizable: false """ +  actUIManagement("forUserDialog") + """ });
$('#forRegisterDialog').dialog({title: '""" + S.??("sign.up") + """', width: 700, height: 365, autoOpen: false, resizable: false """ +   actUIManagement("forRegisterDialog") + """ });
""" 
    )
    Script(OnLoad(initDlgs))
  }
  
  def userMenu : NodeSeq = {         
    def userLogin() : JsCmd = JsRaw("$('#forUserDialog').dialog('open')")
    def userLogout() : JsCmd = try{User.logout}catch{case ex: Exception => JsRaw("")} 
      
    def userRegister() : JsCmd = JsRaw("$('#forRegisterDialog').dialog('open'); triggerCounter(10000);")
    
    if(User.loggedIn_?) <ul><li>{a(() => userLogout, Text(S.??("logout")))}</li></ul>
    else <ul><li>{a(() => userLogin, Text(S.??("login")))}</li><li>{a(() => userRegister, Text(S.??("sign.up")))}</li></ul>
    
    
  }

  def loginStatus = {
    
    if(User.loggedIn_?) {
      val yourName = User.currentUser match {
        case Full(u) => u.userName.is
        case _ => "error"
      }
      <h5>You are: {yourName}</h5><div id="loginico" style="width:68; height:68;">{Avatar.findAll(By(Avatar.lookup, "1")).head.imageUrl}</div>
    } else {
      <h5>You are: anonymous</h5><div id="loginico" style="width:68; height:68;">{Avatar.findAll(By(Avatar.lookup, "2")).head.imageUrl}</div>
    }
  } 

  
  def signUpDialog(doc: NodeSeq): NodeSeq = User.signup(doc)
  
  def loginDialog : NodeSeq = {
            
    var username = ""
    var pwd = ""
    
    def testPwd {
      User.find(By(User.userName, username)).filter(_.password.match_?(pwd)).map{
        u => {logUserIn(u); 
              val dlgs : List[DialogMeta] = dialogInfo.get
              dialogInfo(dlgs.map(x => if(x.id == "forUserDialog") new DialogMeta(x.id,x.position,false) else x))
              logger.info("LOGIN CALLED")
              }
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
