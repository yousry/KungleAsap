package tools

import net.liftweb.http._
import net.liftweb.mapper._
import net.liftweb.common._
import net.liftweb.util.Helpers._

import de.kungle.asap.model._

object AvatarProcessing {

  lazy val imageData: Array[Byte] = {
    val baos = new java.io.ByteArrayOutputStream
    val is = new java.io.FileInputStream("D:/src/KungleAsap/asap/src/main/webapp/gfx/avatar/default.png")
    val buf = new Array[Byte](1024)
    var bytesRead = 0
    while ({ bytesRead = is.read(buf); bytesRead } >= 0) {
        baos.write(buf, 0, bytesRead)
    }
    baos.toByteArray
}
  
   object TestImage {
   def unapply(in: String): Option[Avatar] =
     Avatar.find(By(Avatar.lookup, in.trim))
 }
    
   def matcher: LiftRules.DispatchPF = {
     case req @ Req("avatar" :: TestImage(img) :: Nil, _, GetRequest) =>
     () => {     println("AVATAR MATCHER STARTED"); serveImage(img, req)}
   }
  
   def serveImage(img: Avatar, req: Req) : Box[LiftResponse] = {
      
     println("SERVEImage STARTED")
   
     if (req.testIfModifiedSince(img.saveTime.is+1)) {
       //if not modified, optimized to tell browser to use last good version
       println("Not Modified")
       Full(InMemoryResponse(
         new Array[Byte](0),
         List("Last-Modified" -> toInternetDate(img.saveTime.is)),
         Nil,
         304))
     } else {
       //serve the image
       println("Modified")
       Full(InMemoryResponse(
         img.image.is, // imageData,
         List("Last-Modified" -> toInternetDate(img.saveTime.is),
              "Content-Type" -> "image/png",
              "Content-Length" -> img.image.is.length.toString),
         Nil,
         200)
       )
     }
   }
}
