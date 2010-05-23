package de.kungle.asap.snippet

import scala.xml._

import net.liftweb.common._

import _root_.scala.xml.NodeSeq
import _root_.net.liftweb.util.Helpers
import Helpers._
import _root_.net.liftweb.mapper._
import net.liftweb.http.jquery.JqSHtml
import net.liftweb.http.RequestVar
import net.liftweb.http.S._
import net.liftweb.http.SHtml
import net.liftweb.http.SHtml._
import net.liftweb.http.js.JE._
import net.liftweb.http.js.JsCmds._
import net.liftweb.http.js.{JsCmd,JsExp}
import net.liftweb.http.SessionVar

import _root_.de.kungle.asap.model.User

import de.kungle.asap.model.Comment

class WhatHappensNext extends Loggable {

      
  private var nana : String = ""
  
  private def storePrediction = {
    
    val comment = Comment.create
    
    val user = User.currentUser 
    
    ()
    
  }
  
  def comment(environs: NodeSeq) : NodeSeq = { 
    
    val blargh : () => JsCmd = 
      () => {logger.info("New Prediction: " + nana)
             storePrediction
             JsRaw("$(\"#commentText\").val('');")
      }
    
    val blub = Helpers.bind(
    "preditction", environs, "cmnt" -> textarea("Type your message here!", 
                                                gaga => {nana = gaga}) % ("id" -> "commentText")
                                                                       % ("cols" -> "2") 
                                                					   % ("rows" -> "2")
                                                                       % ("style" -> "resize: none; width:100%; height:30px;"),
    "submit" ->  SHtml.ajaxSubmit("Submit", blargh)
    )
    
    ajaxForm(blub)
  }
}
