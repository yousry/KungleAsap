package de.kungle.fingerprint

import de.kungle.asap.model.Wave

object ShortTermFingerPrintSpec {
  
  def main(args : Array[String]) : Unit = {
    println("Start ShortTermFingerPrintSpec")
        
    println("Check Projection")
    
    import _root_.net.liftweb.mapper._
    import bootstrap.liftweb._
    
    DB.defineConnectionManager(DefaultConnectionIdentifier, DBVendor)
    println(Wave.count)
    
    val search = List("Jewish Family & Children's Services event at Jewish Community Center",
                      "Soldiering on and on")

    val a = new FingerPrintSearch(search)
    a.neighbours.foreach(println(_))
                                                                           
    println("End ShortTermFingerPrintSpec")
    
  }
}
