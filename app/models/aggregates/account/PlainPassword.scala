package models.aggregates.account

import models.shared.atom.{AtomCompanion, StringAtomWrapper}
import org.mindrot.jbcrypt.BCrypt


case class PlainPassword(value: String) extends StringAtomWrapper {

  def hash(): HashedPassword = HashedPassword(BCrypt.hashpw(value, BCrypt.gensalt()))

  def isValid(hashed: HashedPassword): Boolean = BCrypt.checkpw(value, hashed.value)

}
object PlainPassword extends AtomCompanion[PlainPassword]
