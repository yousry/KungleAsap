package de.kungle.api

import net.liftweb._
import common._
import http._
import rest._
import util._
import mapper._
import Helpers._

import de.kungle.asap.model._
import scala.xml.{Node, NodeSeq, Elem}

object PicAPI extends XMLApiHelper {
  
    def createTag(in : NodeSeq) : Elem = <kungle_imp_api>{in}</kungle_imp_api>
 
    def dispatch: LiftRules.DispatchPF = {
      case Req(List("api", "pictures", topic), "", GetRequest) => () => showSource(topic)
    }  
    
      def showSource(topic : String) : LiftResponse = if(isTopicAvail(topic)) Full(buildEntries(topic)): Box[NodeSeq] else Empty: Box[NodeSeq]
    
      def isTopicAvail(topic: String) = {
        val topics = List("Science", "Economy", "Politic", "Technology", "Entertainment", "Sport", "Boulevard", "Adult", "Religion")
        topics.contains(topic)
      }
    
      def buildEntries(topic: String) = {
        val actualDay: Int = {
        		val photoRef = PhotoRef.findAll(OrderBy(PhotoRef.DayOfYear, Descending), MaxRows(1))
        		if(photoRef.isEmpty) 0 else photoRef.head.DayOfYear
        }
       
        val allPhotos = PhotoRef.findAll(By(PhotoRef.DayOfYear, actualDay), By(PhotoRef.Topic, topic))
 
        <entries>{allPhotos.map( calcEntry )}</entries>
      }
      
      def calcEntry(photoRef: PhotoRef): Elem = <entry id={photoRef.PhotoLookup.toString}>
        <publisher>{photoRef.Publisher}</publisher><title>{photoRef.Title}</title>
        <article>{photoRef.ArticleUrl}</article></entry>
      
}
