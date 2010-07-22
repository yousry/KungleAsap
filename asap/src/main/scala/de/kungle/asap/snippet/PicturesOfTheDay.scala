package de.kungle.asap.snippet

import scala.xml._

import net.liftweb.common._

import _root_.scala.xml.NodeSeq
import _root_.net.liftweb.util.Helpers
import Helpers._
import _root_.net.liftweb.mapper._
import net.liftweb.http.jquery.JqSHtml
import net.liftweb.http.S._
import net.liftweb.http.SHtml._
import net.liftweb.http.js.JE._
import net.liftweb.http.js.JsCmds._
import net.liftweb.http.js.{JsCmd,JsExp}

import net.liftweb.http.RequestVar
import net.liftweb.http.SessionVar

import _root_.net.liftweb.http._
import _root_.net.liftweb.http.S._
import net.liftweb.http.js.JE._
import net.liftweb.http.js._
import JsCmds._

import de.kungle.asap.model._
import de.kungle.process.ProcessMaster

import de.kungle.asap.comet._

object myDayOfYear extends SessionVar[Int]({
    val photoRef = PhotoRef.findAll(OrderBy(PhotoRef.DayOfYear, Descending), MaxRows(1))
    if(photoRef.isEmpty) 0 else photoRef.head.DayOfYear
  })

class PicturesOfTheDay {

    val photoRefs = PhotoRef.findAll(By(PhotoRef.DayOfYear, myDayOfYear.get))

    def dayMenu(doc: NodeSeq): NodeSeq = {
      
      val prevDay = PhotoRef.find(By_<(PhotoRef.DayOfYear, myDayOfYear),OrderBy(PhotoRef.DayOfYear, Descending))
      val nextDay = PhotoRef.find(By_>(PhotoRef.DayOfYear, myDayOfYear),OrderBy(PhotoRef.DayOfYear, Ascending))
      
      prevDay match {
        case Full(x) => Text("Day " + x.DayOfYear)
        case _ => Text("...")
      }
      
      //{a(() => {myDayOfYear(x.DayOfYear); S.redirectTo("/")} S.uri
      val calcAnc = (x: PhotoRef) => {a(() => {
        myDayOfYear(x.DayOfYear); JsCmds.RedirectTo("/piclist")
      }, Text("Day " + x.DayOfYear))}
      
      val calcBut = (x: PhotoRef) => ajaxButton(Text("Day " + x.DayOfYear), 
                                                {
                                                  () => myDayOfYear(x.DayOfYear); JsCmds.RedirectTo("/piclist") 
                                                })
      
      Helpers.bind("m", doc, 
                   "day" -> Text("Day " + myDayOfYear),
                   "prev" -> {prevDay match {
                     case Full(x) => calcAnc(x)  
                     case _ => Text("...")
                   }},
                   "next" -> {nextDay match {
                     case Full(x) => calcAnc(x)
                     case _ => Text("...")
                   }}
      )
    }
    
    def queryTabel(doc: NodeSeq): NodeSeq = {
  
      def renderEntry(e: PhotoRef): NodeSeq = bind("entry", chooseTemplate("query", "entries", doc),
      
                                                   "title" -> Text(e.Title),
                                                   "pubdat" -> Text(e.PublishingDate.toString),
                                                   "publisher" -> Text(e.Publisher),
                                                   "photoLink" -> <a href={""+e.ArticleUrl+""} target="_blank"><img src={"/photo/"+e.PhotoLookup}/></a>)
  
      def entries(topic: String): NodeSeq = photoRefs.filter(p => p.Topic.is == topic).flatMap(renderEntry)
      
      bind("query", doc, 
           "entries" -> entries("Science"),
           "economy" -> entries("Economy"),
           "politic" -> entries("Politic"),
           "technology" -> entries("Technology"),
           "entertainment" -> entries("Entertainment"),
           "sport" -> entries("Sport"),
           "boulevard" -> entries("Boulevard"),
           "adult" -> entries("Adult"),
           "religion" -> entries("Religion")
      ) 
}  
  
}
