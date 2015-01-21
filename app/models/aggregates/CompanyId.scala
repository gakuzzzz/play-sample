package models.aggregates

import models.shared.atom.{AtomCompanion, LongAtomWrapper}

case class CompanyId(value: Long) extends LongAtomWrapper
object CompanyId extends AtomCompanion[CompanyId]
