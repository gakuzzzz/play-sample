package controllers.support.json

import _root_.play.api.libs.json.{Json, Format}
import models.aggregates.skill.Skill
import AtomWrapperFormats._
import Iso8601JodaDateTimeFormats._

trait SkillFormats {

  implicit val skillFormats: Format[Skill] = Json.format[Skill]

}
object SkillFormats extends SkillFormats
