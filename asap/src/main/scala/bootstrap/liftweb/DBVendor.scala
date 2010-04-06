package bootstrap.liftweb

import _root_.net.liftweb.common._ 
import net.liftweb.util._
import _root_.net.liftweb.mapper._
import _root_.java.sql._

object DBVendor extends ConnectionManager {

  def newConnection(name: ConnectionIdentifier): Box[Connection] = {
    try {
      Class.forName("com.mysql.jdbc.Driver")
      val dm = DriverManager.getConnection("jdbc:mysql://127.0.0.1/asap_db?user=asap&password=asap")
      Full(dm)
    } catch {
      case e: Exception => e.printStackTrace; Empty
    }
  }

  def releaseConnection(conn: Connection) {conn.close}
  
  
}
