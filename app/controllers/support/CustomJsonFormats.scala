package controllers.support

import controllers.support.json.{SkillFormats, ProgrammerFormats, CompanyFormats, AtomWrapperFormats}
import models.aggregates.programmer.Programmer

trait CustomJsonFormats
    extends AtomWrapperFormats
    with CompanyFormats
    with ProgrammerFormats
    with SkillFormats

object CustomJsonFormats extends CustomJsonFormats
