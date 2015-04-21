package helper

import akka.util.Timeout
import play.api.mvc.{ResponseHeader, Results, Result}
import play.api.test.Helpers._
import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent.{Future, Await}

object TestHelper {

  def contentContains(of: Future[Result], string: String)(implicit timeout: Timeout): Boolean = {
    val result = of.flatMap { result =>
      val eBytes = result.header.headers.get(TRANSFER_ENCODING) match {
        case Some("chunked") => result.body &> Results.dechunk
        case _               => result.body
      }
      eBytes |>>> Iteratees.contains(string.getBytes(charset(result.header).getOrElse("utf-8")))
    }
    Await.result(result, timeout.duration)
  }

  private def charset(header: ResponseHeader): Option[String] = header.headers.get(CONTENT_TYPE) match {
    case Some(s) if s.contains("charset=") => Some(s.split("; charset=").drop(1).mkString.trim)
    case _ => None
  }

}
