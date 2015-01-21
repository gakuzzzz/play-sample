package models.aggregates

import models.shared.atom.{AtomCompanion, LongAtomWrapper}

case class ProgrammerId(value: Long) extends LongAtomWrapper
object ProgrammerId extends AtomCompanion[ProgrammerId]
