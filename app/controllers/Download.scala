package controllers

import org.apache.commons.lang3.StringEscapeUtils
import play.api.http.{HeaderNames, ContentTypes, ContentTypeOf, Writeable}
import play.api.libs.iteratee.Enumerator
import play.api.mvc.{Result, Codec, Controller, Action}
import play.api.libs.concurrent.Execution.Implicits._

trait Download extends Controller {

  import CsvRecord._

  def it: Iterator[CsvRecord] = Iterator.iterate(0)(_ + 1).takeWhile(_ < 30000).map(_.toString).grouped(3).map(s => CsvRecord(s.toSeq))
  // CsvRecord を逐次作成する Enumerator を作成。
  def enum: Enumerator[CsvRecord] = Enumerator.enumerate(it)

  def index = Action {
    // chunked を使って巨大なデータを逐次 Response 出力
    Ok.chunked(enum).withDisposition("fooo.csv")
  }

}

object Download extends Download



/** CSV の1行を表すクラス */
case class CsvRecord(line: Seq[String])
object CsvRecord {

  implicit def writable(implicit codec: Codec): Writeable[CsvRecord] = {
    Writeable(c => codec.encode(c.line.map(StringEscapeUtils.escapeCsv).mkString("", ",", "\n")))
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

