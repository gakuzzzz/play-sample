package models.aggregates

import models.shared.atom.{AtomCompanion, LongAtomWrapper}

case class AccountId(value: Long) extends LongAtomWrapper
object AccountId extends AtomCompanion[AccountId]