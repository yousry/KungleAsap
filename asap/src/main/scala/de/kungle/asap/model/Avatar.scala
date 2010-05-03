package de.kungle.asap.model

import net.liftweb._
import mapper._
import http._
import SHtml._
import util._
import net.liftweb.util.Helpers
import scala.xml._

class Avatar extends LongKeyedMapper[Avatar] with IdPK {
  def getSingleton = Avatar
   
  object image extends MappedBinary(this)
  
  object lookup extends MappedUniqueId(this, 16) {
   override def dbIndexed_? = true
 }
  
  object saveTime extends MappedLong(this) {
   override def defaultValue = Helpers.millis //TimeHelpers method
 }
  
 object mimeType extends MappedString(this, 256)
 def imageUrl : NodeSeq = <img src={"/avatar/"+lookup} />
  
}

object Avatar extends Avatar with LongKeyedMetaMapper[Avatar] {
}