package controllers.support.json

import play.api.libs.json._
import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import play.api.libs.json.JsString
import scala.util.control.Exception._

trait Iso8601JodaDateTimeFormats {

  implicit val iso8601JodaDateTimeFormats: Format[DateTime] = new Format[DateTime] {
    val fmt = ISODateTimeFormat.dateTime()

    override def writes(o: DateTime): JsValue = JsString(fmt.print(o))

    override def reads(json: JsValue): JsResult[DateTime] = json match {
      case JsString(v) => allCatch.either(fmt.parseDateTime(v)).fold(e => JsError(e.getMessage), v => JsSuccess(v))
      case JsNumber(v) => JsSuccess(new DateTime(v))
      case _           => JsError("invalid format")
    }
  }

}
object Iso8601JodaDateTimeFormats extends Iso8601JodaDateTimeFormats