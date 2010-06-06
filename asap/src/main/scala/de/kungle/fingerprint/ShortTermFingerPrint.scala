package de.kungle.fingerprint


import java.util.regex.Pattern
import java.util.regex.Pattern._

import scala.collection.immutable.HashMap

class ShortTermFingerPrint(source: String*) {

  val fingerPrint : Map[String,Int] =  {
    
    
    var result = HashMap[String,Int]()
      
    val allWords = ("\\W".r).
      replaceAllIn((source.toList.mkString(" ")).toLowerCase, " ").
      split(' ').
      map( x => x.trim).
      filter(x => if(x.length < 2) false else if(x.matches(".*[0-9].*")) false else true).
      mkString(" ").
      toList 
    
    def calcTransition(x: (Char,Char)) = if(x._1 != ' ' || x._2 != ' ')
                                            (result += ( List(x._1, x._2).mkString  -> ((result.get(List(x._1, x._2).mkString) getOrElse 0) + 1)))

    if(!allWords.isEmpty)   
    	allWords.zip(allWords.tail).foreach(calcTransition) 
    
    result
  }
  
  val transitions = if(! fingerPrint.isEmpty) fingerPrint.values.toList.reduceLeft[Int](_+_) else 0
  val size = fingerPrint.size
  
  def toList = fingerPrint.toList
  def get(x: String) = fingerPrint.get(x)
  
  def distance(other: ShortTermFingerPrint) : Float = {
    val divisor : Float = Math.max(this.transitions, other.transitions)
    val (fromFiPr, toFiPr) = if(this.size <= other.size)(this, other)else(other,this)
    
    def calcCoincidence(r: Int,comp:(String,Int))= 
      r + Math.min(comp._2 , (toFiPr.get(comp._1) getOrElse 0))
    
    val coincidences : Float = 
      fromFiPr.toList.
      foldLeft(0: Int)(calcCoincidence)
    
    if(divisor == 0) 1 else 1 - coincidences / divisor
  }
  
}
