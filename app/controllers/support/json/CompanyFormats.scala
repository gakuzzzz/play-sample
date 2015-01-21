package controllers.support.json

import play.api.libs.json.{Json, Format}
import models.aggregates.company.Company
import Iso8601JodaDateTimeFormats._

trait CompanyFormats extends AtomWrapperFormats {

  implicit val companyFormats: Format[Company] = Json.format[Company]

}
object CompanyFormats extends CompanyFormats
