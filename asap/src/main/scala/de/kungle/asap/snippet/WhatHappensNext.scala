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

import de.kungle.asap.model.User
import de.kungle.asap.model.Comment

import tools.com.google.api.GoogleAPI;
import tools.com.google.api.translate.Language
import tools.com.google.api.translate.Translate

class WhatHappensNext extends Loggable {
      
  GoogleAPI.setHttpReferrer("http://kungleImp.de/"); // secure init
  private var nana : String = ""
  
  private def storePrediction = {
    
    def LanguageString(l: String) = l match  {
      case "english" => Language.ENGLISH
      case "french" => Language.FRENCH
      case "german" => Language.GERMAN} 
    
    val language = actualLanguage.get
    
    val comment = Comment.create
    comment.originalLanguage(language)
      
    def translate(l: String) : String = {
      var translation = ""
      try {
        translation = Translate.execute(nana, LanguageString(language),  LanguageString(l))
      } catch {
        case ex : Exception => {logger.info("WHN - Google translation exception: " + ex.getMessage ); translation = nana}
      }
      translation
    }
      
      if(language == "english") comment.summary_english(nana) else comment.summary_english(translate("english"))
      if(language == "french") comment.summary_french(nana) else comment.summary_french(translate("french"))
      if(language == "german") comment.summary_german(nana) else comment.summary_german(translate("german"))
    
    User.currentUser match {
      case Full(u) => comment.author(u) 
      case _ => ()
    } 
    
    comment.save
  }
  
  def comment(environs: NodeSeq) : NodeSeq = { 
    
    val blargh : () => JsCmd = 
      () => {logger.info("New Prediction: " + nana)
             storePrediction
             JsRaw("$(\"#commentText\").val('');") // Clear area
      }
    
    val blub = Helpers.bind(
    "preditction", environs, "cmnt" -> textarea("Type your message here!", 
                                                gaga => {nana = gaga}) % ("id" -> "commentText")
                                                                       % ("cols" -> "40") 
                                                					   % ("rows" -> "2")
                                                                       % ("style" -> "resize: none; width:100%; height:30px;"),
    "submit" ->  SHtml.ajaxSubmit("Submit", blargh)
    )

    ajaxForm(blub)
  }
}
