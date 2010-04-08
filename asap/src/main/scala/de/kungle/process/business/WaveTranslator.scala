package de.kungle.process.business

import tools.com.google.api.GoogleAPI;
import tools.com.google.api.translate.Language
import tools.com.google.api.translate.Translate

import _root_.net.liftweb.mapper._
import de.kungle.asap.model._

import net.liftweb.common._
import net.liftweb.util.TimeHelpers._

class WaveTranslator(w : Wave) extends Loggable {

  if(WaveTranslator.initialized)
    logger.info("Starting Wave Translation at at: %s".format(now))
  else
    logger.info("Google API referrer not initialised  at: %s".format(now))
  
  w.title_german(Translate.execute(w.title_english.is,Language.ENGLISH, Language.GERMAN))
  w.title_french(Translate.execute(w.title_english.is,Language.ENGLISH, Language.FRENCH))

  w.summary_german(Translate.execute(w.summary_english.is,Language.ENGLISH, Language.GERMAN))
  w.summary_french(Translate.execute(w.summary_english.is,Language.ENGLISH, Language.FRENCH))
  
  w.translator("Google Translation")
  w.save
}

object WaveTranslator {
  
  
    GoogleAPI.setHttpReferrer("http://distdev.net/");
    val initialized = true

}