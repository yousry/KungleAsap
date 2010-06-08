package de.kungle.fingerprint

import de.kungle.asap.model.Wave

import scala.swing._
import java.awt._
import javax.swing._


object ShortTermFingerPrintSpec {
  
  def main(args : Array[String]) : Unit = {
    println("Start ShortTermFingerPrintSpec")
        
    println("Check Projection")
    
    import _root_.net.liftweb.mapper._
    import bootstrap.liftweb._
    
    DB.defineConnectionManager(DefaultConnectionIdentifier, DBVendor)
    println(Wave.count)
    
    val search = "Invercargill, New Zealand"
 
    
    for(w <- Wave.findAll) {
      println(w.title_english  + " pos: " + FingerPrintProjector.project(w))
    }
                                                                           
    println("End ShortTermFingerPrintSpec")
    
  }
}
