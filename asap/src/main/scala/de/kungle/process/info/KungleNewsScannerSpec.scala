package de.kungle.process.info

import _root_.net.liftweb.common._
import _root_.net.liftweb.util._
import _root_.net.liftweb.http._
import _root_.net.liftweb.sitemap._
import _root_.net.liftweb.sitemap.Loc._
import Helpers._
import _root_.net.liftweb.mapper._
import de.kungle.asap.model._
import bootstrap.liftweb._

object KungleNewsScannerSpec {
  def main(args : Array[String]) : Unit = {

        
    //Logger.setup
    try {
      println("init Logging")
      LiftRules.configureLogging
    } catch {
      case ex: Exception => println("FUU: " + ex)
    }

    
    println("Test Scanner Spec")
    
    // bind mysql connection to mapper
    DB.defineConnectionManager(DefaultConnectionIdentifier, DBVendor)
    
      
    // create a KungleScanner
    val kungleScanner = new KungleScanner()
    println("Scanner created")
    
    // scan kungles top news
    try {
    	kungleScanner.scan
    } catch {
      case ex : Exception => println(ex)
    }
    
    println("Kungle Top News scanned")
    
  }
}
