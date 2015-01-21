package models.aggregates.programmer

import org.joda.time.DateTime
import models.aggregates.company.Company
import models.aggregates.skill.Skill
import models.aggregates.{CompanyId, ProgrammerId}

case class Programmer(
    id: ProgrammerId,
    name: String,
    companyId: Option[CompanyId] = None,
    company: Option[Company] = None,
    skills: Seq[Skill] = Nil,
    createdAt: DateTime,
    deletedAt: Option[DateTime] = None) {

}
