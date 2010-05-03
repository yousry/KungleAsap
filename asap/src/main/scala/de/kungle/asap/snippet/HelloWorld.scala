package de.kungle.asap {
package snippet {

import _root_.scala.xml.NodeSeq
import _root_.net.liftweb.util.Helpers
import Helpers._
import model._

class HelloWorld {
  def howdy(in: NodeSeq): NodeSeq =
    Helpers.bind("b", in, "time" -> (new _root_.java.util.Date).toString)
  
  def checkImage(in:NodeSeq): NodeSeq =  Avatar.findAll.head.imageUrl
  
}

}
}
