package integration

import akka.util.Timeout
import org.scalatest.FunSpec
import play.api.Application
import play.api.libs.iteratee.{Enumerator, Enumeratee, Iteratee}
import play.api.mvc.{Results, Result}
import play.api.test.{FakeRequest, FakeApplication}
import play.api.test.Helpers._

import scala.concurrent.{Future, Await}


class DownloadSpec extends FunSpec {

  describe("#index") {
    it("contains value") {
      running(FakeApplication()) {
        val res = route(FakeRequest(GET, "/download")).get
        assert(status(res) == 200)
        assert(chunkedContentAsString(res).contains(", 1234,"))
      }
    }
  }

  def chunkedContentAsString(of: Future[Result])(implicit timeout: Timeout): String = new String(chunkedContentAsBytes(of), charset(of).getOrElse("utf-8"))

  def chunkedContentAsBytes(of: Future[Result])(implicit timeout: Timeout): Array[Byte] = {
    val body = of.flatMap { result =>
      val eBytes = result.header.headers.get(TRANSFER_ENCODING) match {
        case Some("chunked") => result.body &> Results.dechunk
        case _               => result.body
      }
      eBytes |>>> Iteratee.consume[Array[Byte]]()
    }
    Await.result(body, timeout.duration)
  }

  case class MatchState(matched: Seq[Char], unmatch: Seq[Char])

  def contentContains(of: Future[Result], containee: String)(implicit timeout: Timeout): Boolean = {
    val body = of.flatMap { result =>
      val cset = charset(of).getOrElse("utf-8")
      val eBytes = result.header.headers.get(TRANSFER_ENCODING) match {
        case Some("chunked") => result.body &> Results.dechunk
        case _               => result.body
      }
      eBytes.map(s => new String(s, cset))


      ???
    }





    ???
  }

}