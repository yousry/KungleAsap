package bootstrap.liftweb

import _root_.net.liftweb.common._
import _root_.net.liftweb.util._
import _root_.net.liftweb.http._
import _root_.net.liftweb.sitemap._
import _root_.net.liftweb.sitemap.Loc._
import Helpers._
import _root_.net.liftweb.mapper._

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

    // Build SiteMap
    val entries = 
      Menu(Loc("Home", List("index"), "Home")) :: 
      Menu(Loc("Control",("admin" :: "control" :: Nil) -> false,"Control")) ::
      Nil
    LiftRules.setSiteMap(SiteMap(entries:_*))
  }
}

