package models.aggregates.account

import models.shared.atom.{AtomCompanion, StringAtomWrapper}

case class HashedPassword private[account] (value: String) extends StringAtomWrapper
object HashedPassword extends AtomCompanion[HashedPassword]
