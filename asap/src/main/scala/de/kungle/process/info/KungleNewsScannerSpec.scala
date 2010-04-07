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
    
    println("Start Scanner")
    
    // bind mysql connection to mapper
    DB.defineConnectionManager(DefaultConnectionIdentifier, DBVendor)

    // create a KungleScanner
    
  }
}
