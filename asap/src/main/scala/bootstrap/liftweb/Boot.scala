package bootstrap.liftweb

import _root_.net.liftweb.common._
import _root_.net.liftweb.util._
import _root_.net.liftweb.http._
import _root_.net.liftweb.sitemap._
import _root_.net.liftweb.sitemap.Loc._
import Helpers._
import _root_.net.liftweb.mapper._
import de.kungle.asap.model._

import _root_.net.liftweb.widgets.autocomplete._


import de.kungle.process.business.{InfoCollector, TranslationCollector, SimpleCategorize, SimpleDictionaryUpdate }

import de.kungle.asap.snippet.{WaveJason}

import tools.AvatarProcessing

/**
  * A class that's instantiated early and run.  It allows the application
  * to modify lift's environment
  */
class Boot {
  def boot {
    
        
    // bind mysql connection to mapper
    DB.defineConnectionManager(DefaultConnectionIdentifier, DBVendor)
    
    // where to search snippet
    LiftRules.addToPackages("de.kungle.asap")
    LiftRules.addToPackages("tools")

    // DDL
    Schemifier.schemify(true, Schemifier.infoF _, Comment, User, UserLog, Wave, DictionaryEntry, Avatar)
    
    // Build SiteMap
    val entries = 
      Menu(Loc("Home", List("index"), "Home", LocGroup("default"))) :: 
      Menu(Loc("Control",("admin" :: "control" :: Nil) -> false,"Control", LocGroup("default"))) ::
      User.sitemap
    
    LiftRules.setSiteMap(SiteMap(entries:_* ))

    AutoComplete.init
    
    LiftRules.snippetDispatch.append((Map("WaveJason" -> WaveJason)))
    
    // LiftRules.jsArtifacts = net.liftweb.http.js.jquery.JQuery14Artifacts
    
    // Avatar Processor as Dispatcher
    LiftRules.dispatch.append(AvatarProcessing.matcher)
    
    //  start Actors
    ActorPing.schedule(InfoCollector, InfoCollector.DoWork, 1 seconds)
    ActorPing.schedule(TranslationCollector, TranslationCollector.DoWork, 10 seconds)
    ActorPing.schedule(SimpleCategorize, SimpleCategorize.DoWork, 20 seconds)
    ActorPing.schedule(SimpleDictionaryUpdate, SimpleDictionaryUpdate.DoWork, 30 seconds)
    
  }
}

