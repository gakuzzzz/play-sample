package integration

import akka.util.Timeout
import org.scalatest.FunSpec
import play.api.Application
import play.api.mvc.{ResponseHeader, Results, Result}
import play.api.test.{FakeRequest, FakeApplication}
import play.api.test.Helpers._
import play.api.libs.iteratee._
import scala.concurrent.ExecutionContext.Implicits._
import scala.concurrent.{Future, Await}
import scala.concurrent.duration._

class DownloadSpec extends FunSpec {

  describe("#index") {
    it("contains value") {
      running(FakeApplication()) {
        val res = route(FakeRequest(GET, "/download")).get
        assert(status(res) == 200)
        assert(contentContains(res, ", 1234,"))
      }
    }
  }

  describe("contains") {
    it("should check split input") {
      val e = Enumerator("ab", "cd", "ef").map(_.getBytes)
      val f = e |>>> contains("bc".getBytes)
      assert(Await.result(f, 100.seconds))
    }
  }


  def contentContains(of: Future[Result], string: String)(implicit timeout: Timeout): Boolean = {
    val body = of.flatMap { result =>
      val eBytes = result.header.headers.get(TRANSFER_ENCODING) match {
        case Some("chunked") => result.body &> Results.dechunk
        case _               => result.body
      }
      eBytes |>>> contains(string.getBytes(charset(result.header).getOrElse("utf-8")))
    }
    Await.result(body, timeout.duration)
  }

  def charset(header: ResponseHeader): Option[String] = header.headers.get(CONTENT_TYPE) match {
    case Some(s) if s.contains("charset=") => Some(s.split("; charset=").drop(1).mkString.trim)
    case _ => None
  }

  def contains(string: Array[Byte]): Iteratee[Array[Byte], Boolean] = {
    def step(k: Int): Input[Array[Byte]] => Iteratee[Array[Byte], Boolean] = {
      case Input.El(bytes) =>
        @scala.annotation.tailrec
        def cursor(i: Int, k: Int): Option[Int] =
          if (k == string.length) None
          else if (i == bytes.length) Some(k)
          else if (bytes(i) == string(k)) cursor(i + 1, k + 1)
          else cursor(i + 1, 0)
        cursor(0, k).map(k => Cont(step(k))).getOrElse(Done(true, Input.Empty))
      case Input.Empty => Cont(step(k))
      case Input.EOF => Done(false, Input.EOF)
    }
    Cont(step(0))
  }

}