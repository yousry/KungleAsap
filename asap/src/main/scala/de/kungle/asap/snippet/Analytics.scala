package de.kungle.asap.snippet

import scala.xml._

import net.liftweb.common._

import _root_.scala.xml.NodeSeq
import _root_.net.liftweb.util.Helpers
import Helpers._
import _root_.net.liftweb.mapper._
import net.liftweb.http.jquery.JqSHtml
import net.liftweb.http.S._
import net.liftweb.http.SHtml._
import net.liftweb.http.js.JE._
import net.liftweb.http.js.JsCmds._
import net.liftweb.http.js.{JsCmd,JsExp}

import net.liftweb.http.RequestVar
import net.liftweb.http.SessionVar

import _root_.net.liftweb.http._
import _root_.net.liftweb.http.S._
import net.liftweb.http.js.JE._
import net.liftweb.http.js._
import JsCmds._

import de.kungle.asap.model._
import de.kungle.process.ProcessMaster

import de.kungle.asap.comet._


class Analytics {

  def analysisScript: JsCmd = JsRaw("""var _gaq = _gaq || [];
  _gaq.push(['_setAccount', 'UA-10175436-2']);
  _gaq.push(['_trackPageview']);

  (function() {
    var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
    ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
  })();""") 
  
  def analysis(doc: NodeSeq): NodeSeq  = Script(analysisScript)
  
}
