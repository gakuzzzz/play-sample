package views.programmers

import models.aggregates.skill.Skill
import models.aggregates.company.Company
import models.aggregates.ProgrammerId
import org.joda.time.DateTime

case class ProgrammerView(
    id: ProgrammerId,
    name: String,
    createdAt: DateTime,
    updatedAt: DateTime,
    company: Option[Company],
    skills: Seq[Skill]
)
