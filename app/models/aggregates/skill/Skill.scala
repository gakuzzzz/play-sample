package models.aggregates.skill

import org.joda.time.DateTime
import models.aggregates.SkillId

case class Skill(
    id: SkillId,
    name: String,
    createdAt: DateTime,
    deletedAt: Option[DateTime] = None) {

}
