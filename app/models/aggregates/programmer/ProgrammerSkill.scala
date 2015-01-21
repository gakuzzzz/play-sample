package models.aggregates.programmer

import models.aggregates.{SkillId, ProgrammerId}

private[programmer] case class ProgrammerSkill(programmerId: ProgrammerId, skillId: SkillId)

