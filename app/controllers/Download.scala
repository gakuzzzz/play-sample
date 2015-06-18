package controllers

import controllers.stack.TxElement
import models.ComponentRegistry
import models.aggregates.programmer.Programmer
import models.services.ServiceComponents
import models.shared.PlaySampleContext
import org.apache.commons.lang3.StringEscapeUtils
import play.api.http.{HeaderNames, ContentTypes, ContentTypeOf, Writeable}
import play.api.libs.iteratee.Enumerator
import play.api.mvc.{Result, Codec, Controller, Action}
import play.api.libs.concurrent.Execution.Implicits._
import scalikejdbc.DBSession

trait Download extends Controller with TxElement { self: ServiceComponents =>

  // dummy implementation
  private def totalCount: Long = 4
  // dummy implementation
  private def paging(limit: Int, offset: Int)(implicit session: DBSession): Seq[Programmer] = {
    programmerService.findSubset(limit, offset)
  }

  private def csvEnumerator(implicit session: DBSession): Enumerator[CsvRecord] = {
    val limit = 2
    val offsets = Iterator.iterate(0)(_ + limit).takeWhile(_ <= totalCount)
    val header = Enumerator(CsvRecord("id", "name", "company_id", "created_at"))
    val body = for {
      offset     <- Enumerator.enumerate(offsets)
      programmer <- Enumerator.enumerate(paging(limit, offset))
    } yield CsvRecord(programmer.id, programmer.name, programmer.companyId, programmer.createdAt)
    header >>> body
  }

  def index = StackAction { implicit req =>
    import CsvRecord._
    Ok.chunked(csvEnumerator).withDisposition("fooo.csv")
  }

}

object Download extends Download with ComponentRegistry



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

