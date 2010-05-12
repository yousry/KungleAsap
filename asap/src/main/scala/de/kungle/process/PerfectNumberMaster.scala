package de.kungle.process

import net.liftweb.actor._
import _root_.net.liftweb._
import http._
import util._
import Helpers._
import net.liftweb.common._
import net.liftweb.mapper._

import de.kungle.asap.model.PerfectNumber
import de.kungle.asap.comet._

import de.kungle.asap.snippet.Crunchy

import scala.collection.mutable.HashMap

object PerfectNumberMaster extends LiftActor with Loggable{
  
  private val CALCULATIONS_PER_TICKET = 10000
  
  var perfectNumberClients = HashMap[PerfectNumberStatus, Int]()

  var perfectNumbers = PerfectNumber.findAll(OrderBy(PerfectNumber.MyNumber, Descending))
  
  var lastPerfectNumber : PerfectNumber = if(perfectNumbers.isEmpty) { 
    val firstPefectNumber = 
      new PerfectNumber MyNumber(6) LastCheckedNumber(6) DonatedCalculations(0)
    firstPefectNumber.save
    firstPefectNumber  
  } else perfectNumbers.head
  
  var tickets = new HashMap[Int,Boolean]()
  var factors = List[Double]()
  
  def newNumber : Double =  lastPerfectNumber.LastCheckedNumber.is + 1.0 
  
  def calcTickets = {
    val upperLimit =  Math.ceil(Math.sqrt(newNumber)) // ? 
    val MaxTicket : Int = Math.max(1, upperLimit.toInt / CALCULATIONS_PER_TICKET)
    (1 to MaxTicket).foreach(i => tickets += (i -> false))
  }
  
  calcTickets
  
  def calcCrunchy : Crunchy = {
    
    var firstWatingTicket = tickets.toList.find(x => x._2 == false) getOrElse tickets.toList.head
    firstWatingTicket = (firstWatingTicket._1,true) // working
    tickets += firstWatingTicket
    val start : Double = Math.max(1, (firstWatingTicket._1 - 1) * CALCULATIONS_PER_TICKET)
    val end : Double = firstWatingTicket._1 * CALCULATIONS_PER_TICKET
    new Crunchy(firstWatingTicket._1, start, end, newNumber)
  }
  
  def employClient(pstat: PerfectNumberStatus )  = {
    logger.info("start employing " + pstat)
    perfectNumberClients -= pstat
    logger.info("old entry removed " + pstat)
    val nextCrunch = calcCrunchy
    logger.info("delegate job to " + pstat)
    
    val crunch = nextCrunch
    pstat ! DoCrunch(crunch)
        
    lastPerfectNumber.DonatedCalculations( lastPerfectNumber.DonatedCalculations.get + (Math.min(crunch.end, crunch.master ) -  crunch.start ).toLong  )
    lastPerfectNumber.save

    logger.info("DONE")
    perfectNumberClients += (pstat -> nextCrunch.ticket)
  }
    
  def checkAndNext() = {
    
    
    val genFactors  = factors.remove(x => x == newNumber).removeDuplicates
    
    if((0.0 /: genFactors) {(sum, x) => x + sum} == newNumber) { // newPerfectNumber Found
      lastPerfectNumber.save
      val nextPerfectNumber = new PerfectNumber
      nextPerfectNumber.MyNumber(newNumber) LastCheckedNumber(newNumber) DonatedCalculations(lastPerfectNumber.DonatedCalculations.get)
      lastPerfectNumber = nextPerfectNumber
    } else {// just update
      lastPerfectNumber.LastCheckedNumber(newNumber)
      lastPerfectNumber.save 
    }
    factors = List()
    calcTickets
  }
  
  def reEmploy(employeTicket: Int, employeResult: List[Double])  = {
    
    logger.info("Try to ReEmploy")
    
    tickets -= (employeTicket) // remove or nothing
    factors ++= employeResult
    factors.removeDuplicates
 
    if( tickets.isEmpty) {
      checkAndNext
    }
    
    logger.info("Searching for ticket holder: " + employeTicket)
    logger.info("Avaliable employes: " + perfectNumberClients.toList )
    
    perfectNumberClients.toList.find(x => x._2 == employeTicket) match {
      case Some(e)  => {logger.info("ReEmploy " + e._1);  employClient(e._1) }
      case _  => logger.info("Employe not found")
    }
  }

  
  protected def messageHandler = {
    case CalcDone(employeTicket, employeResult) => {
      logger.info("CalcDone Ticket: " + employeTicket)
      logger.info("CalcDone Factors: " + employeResult)

      reEmploy(employeTicket, employeResult)
    }
    case SubscribePerfectNumberMaster(pstat) => employClient(pstat)
    case UnSubscribePerfectNumberMaster(pstat) => perfectNumberClients  -= pstat
    case m => logger.error("PerfectNumberMaster Unidentified Command: " + m)
  }
    
  case class SubscribePerfectNumberMaster(pStat: PerfectNumberStatus)
  case class UnSubscribePerfectNumberMaster(pStat: PerfectNumberStatus)
  case class CalcDone(ticketId: Int, factors: List[Double])
}
