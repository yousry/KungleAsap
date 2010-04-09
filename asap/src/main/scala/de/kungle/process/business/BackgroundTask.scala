package de.kungle.process.business

trait BackgroundTask {

  var scheduleIntervall : Long
  var lastRun : Option[java.util.Date] = None
  
  case object GetStatus
  case object SwitchStatus
  case object Changeintervall
  case object LastPass
  case object DoWork
}
