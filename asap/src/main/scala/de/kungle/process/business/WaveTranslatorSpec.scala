package de.kungle.process.business

import _root_.net.liftweb.common._
import _root_.net.liftweb.util._
import _root_.net.liftweb.http._
import _root_.net.liftweb.sitemap._
import _root_.net.liftweb.sitemap.Loc._
import Helpers._
import _root_.net.liftweb.mapper._
import de.kungle.asap.model._
import bootstrap.liftweb._

object WaveTranslatorSpec {
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
    
    val untranslatedWaves = Wave.findAll(By(Wave.translator,"none"))
    
    println("Waves to translate: " + untranslatedWaves.length)
    
    untranslatedWaves.foreach(new WaveTranslator(_))
    
    println("Translation Done.")
  }
}
