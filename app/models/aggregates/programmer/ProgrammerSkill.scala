package models.aggregates.programmer

import models.aggregates.{SkillId, ProgrammerId}
import scalikejdbc._

private[programmer] case class ProgrammerSkill(programmerId: ProgrammerId, skillId: SkillId)

