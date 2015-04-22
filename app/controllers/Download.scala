package controllers

import models.aggregates.ProgrammerId
import models.aggregates.programmer.Programmer
import org.apache.commons.lang3.StringEscapeUtils
import org.joda.time.DateTime
import play.api.http.{HeaderNames, ContentTypes, ContentTypeOf, Writeable}
import play.api.libs.iteratee.Enumerator
import play.api.mvc.{Result, Codec, Controller, Action}
import play.api.libs.concurrent.Execution.Implicits._

trait Download extends Controller {

  // dummy implementation
  private def totalCount: Long = 12345
  // dummy implementation
  private def paging(limit: Int, offset: Int): Seq[Programmer] = {
    val ids = (offset + 1) to ((limit + offset) min totalCount.toInt)
    ids.map { i =>
      Programmer(ProgrammerId(i), s"name$i", None, DateTime.now(), DateTime.now())
    }.toSeq
  }

  private def csvEnumerator: Enumerator[CsvRecord] = {
    val limit = 300
    val offsets = Iterator.iterate(0)(_ + limit).takeWhile(_ < totalCount)
    val header = Enumerator(CsvRecord("id", "name", "company_id", "created_at"))
    val body = for {
      offset     <- Enumerator.enumerate(offsets)
      programmer <- Enumerator.enumerate(paging(limit, offset))
    } yield CsvRecord(programmer.id, programmer.name, programmer.companyId, programmer.createdAt)
    header >>> body
  }

  def index = Action {
    import CsvRecord._
    Ok.chunked(csvEnumerator).withDisposition("fooo.csv")
  }

}

object Download extends Download



/** CSV の1行を表すクラス */
case class CsvRecord(line: Any*)
object CsvRecord {

  implicit def writable(implicit codec: Codec): Writeable[CsvRecord] = Writeable { c =>
    val line = c.line.map(e => StringEscapeUtils.escapeCsv(e.toString)).mkString("", ",", "\n")
    codec.encode(line)
  }

  implicit def contentTypeOf(implicit codec: Codec): ContentTypeOf[CsvRecord] = {
    ContentTypeOf[CsvRecord](Some(ContentTypes.withCharset("text/comma-separated-values")))
  }

  implicit class ResultOps(val value: Result) extends AnyVal {
    def withDisposition(fileName: String): Result = {
      value.withHeaders(HeaderNames.CONTENT_DISPOSITION -> s"""filename="$fileName"""")
    }
  }

}

