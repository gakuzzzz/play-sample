package models.aggregates.programmer

import org.joda.time.DateTime
import models.aggregates.{SkillId, CompanyId, ProgrammerId}

case class Programmer(
    id: ProgrammerId,
    name: String,
    companyId: Option[CompanyId],
    createdAt: DateTime,
    updatedAt: DateTime,
    deletedAt: Option[DateTime] = None,
    version: Int,
    skillIds: Seq[SkillId] = Nil) {

}
