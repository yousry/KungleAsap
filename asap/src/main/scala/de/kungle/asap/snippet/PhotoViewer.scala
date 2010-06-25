package de.kungle.asap {
package snippet {

import _root_.scala.xml.NodeSeq
import _root_.net.liftweb.util.Helpers
import Helpers._
import model._  
  
class PhotoViewer {

  def view (doc: NodeSeq): NodeSeq = {
    
    Helpers.bind("photo", doc, 
                 "image" ->   <img src={"/photo/Example1"} />
    )
    
  }
  
}

}
}