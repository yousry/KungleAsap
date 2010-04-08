package de.kungle.process.business

import tools.com.google.api.GoogleAPI;
import tools.com.google.api.translate.Language
import tools.com.google.api.translate.Translate

import _root_.net.liftweb.mapper._
import de.kungle.asap.model._

import net.liftweb.common._
import net.liftweb.util.TimeHelpers._

class WaveTranslator(w : Wave) extends Loggable {

  logger.info("Starting Wave Translation at at: %s".format(now))
  
  w.title_german(WaveTranslator.transExec(w.title_english.is,Language.ENGLISH, Language.GERMAN))
  w.title_french(WaveTranslator.transExec(w.title_english.is,Language.ENGLISH, Language.FRENCH))

  w.summary_german(WaveTranslator.transExec(w.summary_english.is,Language.ENGLISH, Language.GERMAN))
  w.summary_french(WaveTranslator.transExec(w.summary_english.is,Language.ENGLISH, Language.FRENCH))
  
  w.translator("Google Translation")
  w.save
}

object WaveTranslator extends Loggable {

  // Say Hello to Google
  GoogleAPI.setHttpReferrer("http://distdev.net/");
    
  // get rid of the EXCEPTION LOGIC
  def transExec(i: String, s: Language, d: Language) : String = {
    var translation = "not translated"
      try{
        translation = Translate.execute(i, s, d)
      } catch {
        case ex : Exception => {
          translation = "Tranlation not possible"
          logger.info("Google translation exception: " + ex.getStackTraceString )
          ex.printStackTrace

        }
      }
      translation
    }
}