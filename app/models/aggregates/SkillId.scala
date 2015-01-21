package models.aggregates

import models.shared.atom.{AtomCompanion, LongAtomWrapper}

case class SkillId(value: Long) extends LongAtomWrapper
object SkillId extends AtomCompanion[SkillId]
