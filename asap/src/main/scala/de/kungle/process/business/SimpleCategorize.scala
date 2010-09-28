package de.kungle.process.business

import net.liftweb.actor._
import _root_.net.liftweb._
import http._
import util._
import Helpers._
import net.liftweb.common._
import de.kungle.process.info.KungleScanner
import _root_.net.liftweb.mapper._
import de.kungle.process.ProcessMaster

object SimpleCategorize extends LiftActor with BackgroundTask with Loggable {

 override var scheduleIntervall : Long = 10 // 10 Minutes intervall
  
      protected def messageHandler = {
      case DoWork => work
      case m => logger.error("Scheduler Unidentified Command: " + m)
    }
  
  def work() = {

    val topicUpdate = "update wave set picurl = ? where topic = ?"
    val personUpdate = "update wave set picurl = ? where title_english like '%obama%' or summary_english like '%obama%';"
    
    DB.use(DefaultConnectionIdentifier) { 
      conn => DB.prepareStatement(topicUpdate , conn) {  
        stmt => stmt.setString(1, "/thumbnails/topic/sport.png")
                stmt.setString(2, "Sport")
        		stmt.executeUpdate } 
    }
        
    DB.use(DefaultConnectionIdentifier) { 
      conn => DB.prepareStatement(topicUpdate , conn) {  
        stmt => stmt.setString(1, "/thumbnails/topic/economy.png")
                stmt.setString(2, "Economy")
        		stmt.executeUpdate } 
    }
        
    DB.use(DefaultConnectionIdentifier) { 
      conn => DB.prepareStatement(topicUpdate , conn) {  
        stmt => stmt.setString(1, "/thumbnails/topic/science.png")
                stmt.setString(2, "Science")
        		stmt.executeUpdate } 
    }

        DB.use(DefaultConnectionIdentifier) { 
      conn => DB.prepareStatement(topicUpdate , conn) {  
        stmt => stmt.setString(1, "/thumbnails/topic/politic.png")
                stmt.setString(2, "Politic")
        		stmt.executeUpdate } 
    }

        DB.use(DefaultConnectionIdentifier) { 
      conn => DB.prepareStatement(topicUpdate , conn) {  
        stmt => stmt.setString(1, "/thumbnails/topic/technology.png")
                stmt.setString(2, "Technology")
        		stmt.executeUpdate } 
    }

                
        DB.use(DefaultConnectionIdentifier) { 
      conn => DB.prepareStatement(topicUpdate , conn) {  
        stmt => stmt.setString(1, "/thumbnails/topic/boulevard.png")
                stmt.setString(2, "Boulevard")
        		stmt.executeUpdate } 
    }

        DB.use(DefaultConnectionIdentifier) { 
      conn => DB.prepareStatement(topicUpdate , conn) {  
        stmt => stmt.setString(1, "/thumbnails/topic/entertainment.png")
                stmt.setString(2, "Entertainment")
        		stmt.executeUpdate } 
    }
        
    
    DB.use(DefaultConnectionIdentifier) { 
      conn => DB.prepareStatement(personUpdate , conn) {  
        stmt => stmt.setString(1, "/thumbnails/person/obama.png")
        		stmt.executeUpdate } 
    }
    
    ProcessMaster ! ProcessMaster.UpdateStatus("SimpleCategorize")    
    logger.info("SimpleCategorize finished.")
    ActorPing.schedule(SimpleCategorize, SimpleCategorize.DoWork, scheduleIntervall minutes)
    
  }
  
}
