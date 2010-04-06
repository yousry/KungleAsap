package de.kungle.process.business

trait BackgroundTask {

  var scheduleIntervall : Long
  var lastRun : Option[java.util.Date] = None
  
  case object getStatus
  case object changeintervall
  case object lastPass
}
