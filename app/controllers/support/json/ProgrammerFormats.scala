package controllers.support.json

import play.api.libs.json.{Json, Format}
import models.aggregates.programmer.Programmer
import SkillFormats._
import CompanyFormats._
import Iso8601JodaDateTimeFormats._
import views.programmers.ProgrammerView

trait ProgrammerFormats extends CompanyFormats with AtomWrapperFormats {

  implicit val programmerFormats: Format[Programmer] = Json.format[Programmer]

  implicit val programmerViewWrites: Format[ProgrammerView] = Json.format[ProgrammerView]

}
object ProgrammerFormats extends ProgrammerFormats

