package de.kungle.fingerprint
  
import de.kungle.asap.model.Wave

class FingerPrintSearch(search: List[String]) {
  
  def this(s: String*) = this(s.toList)
  
  val MAX_TERMS = 10
    
  val searchTerms = search.take(MAX_TERMS)
  val searchTermsFP = searchTerms.map(x => new ShortTermFingerPrint(x))
  
  val neighbours = FingerPrintSearch.waves.
    map(w => (w._1,searchTermsFP.foldLeft(0d)((s, st) => s + st.distance(w._2)))).
    sort((x,y) => x._2 <= y._2).
    take(MAX_TERMS). 
    map(x => x._1)
}

object FingerPrintSearch {
  import net.liftweb.mapper._
    
  val MAX_SEARCHFIELD = 10000
  val UPDATE_INTERVALL = 1000 * 60 * 10
  
  var _waves = Wave.findAll(OrderBy(Wave.id, Descending), MaxRows(MAX_SEARCHFIELD)).map(x => (x.id, new ShortTermFingerPrint(x.title_english)))
  var _lastUpdate = new java.util.Date
  
  def  waves =  synchronized (if((new java.util.Date).getTime <= _lastUpdate.getTime + UPDATE_INTERVALL) _waves else {
    _lastUpdate = new java.util.Date
    _waves = Wave.findAll(OrderBy(Wave.id, Descending), MaxRows(MAX_SEARCHFIELD)).map(x => (x.id, new ShortTermFingerPrint(x.title_english)))
   _waves
  })
  
}