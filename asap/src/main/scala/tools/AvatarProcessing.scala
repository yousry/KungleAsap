package tools

import net.liftweb.http._
import net.liftweb.mapper._
import net.liftweb.common._
import net.liftweb.util.Helpers._

import de.kungle.asap.model._

object AvatarProcessing extends Loggable{

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
  
   object TestAvatar {def unapply(in: String): Option[Avatar] = Avatar.find(By(Avatar.lookup, in.trim))}
   object TestPhoto  {def unapply(in: String): Option[Photo] = Photo.find(By(Photo.lookup, in.trim))}
   
   def matcher: LiftRules.DispatchPF = {
     case req @ Req("avatar" :: TestAvatar(img) :: Nil, _, GetRequest) => () => {logger.info("AVATAR MATCHER STARTED"); serveImage(img, req)}
     case req @ Req("photo" :: TestPhoto(img) :: Nil, _, GetRequest) => () => {logger.info("AVATAR MATCHER STARTED"); serveImage(img, req)}
   }
   
   def serveImage(img: Photo, req: Req) : Box[LiftResponse] = {
          logger.info("SERVE PHOTO STARTED")
     if (req.testIfModifiedSince(img.saveTime.is+1)) {
       logger.info("Photo Not Modified")
       Full(InMemoryResponse(new Array[Byte](0),List("Last-Modified" -> toInternetDate(img.saveTime.is)),Nil,304))
     } else {
       logger.info("Photo Modified")
       Full(InMemoryResponse(img.image.is, 
                             List("Last-Modified" -> toInternetDate(img.saveTime.is),
                             "Content-Type" -> "image/jpeg",
                             "Content-Length" -> img.image.is.length.toString),
                             Nil, 200))
     }
   } 
   
   def serveImage(img: Avatar, req: Req) : Box[LiftResponse] = {
     logger.info("SERVEImage STARTED")
     if (req.testIfModifiedSince(img.saveTime.is+1)) {
       logger.info("Not Modified")
       Full(InMemoryResponse(new Array[Byte](0),List("Last-Modified" -> toInternetDate(img.saveTime.is)),Nil,304))
     } else {
       logger.info("Modified")
       Full(InMemoryResponse(img.image.is, 
                             List("Last-Modified" -> toInternetDate(img.saveTime.is),
                             "Content-Type" -> "image/png",
                             "Content-Length" -> img.image.is.length.toString),
                             Nil, 200))
     }
   }
}
