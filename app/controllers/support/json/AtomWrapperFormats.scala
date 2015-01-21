package controllers.support.json

import models.shared.atom.{AtomCompanion, AtomWrapper}
import play.api.libs.json._

trait AtomWrapperFormats {

  def atomWrapperReads[A <: AtomWrapper]
      (implicit companion: AtomCompanion[A], base: Reads[A#ValueType]): Reads[A] = {
    base.map(companion.apply)
  }

  def atomWrapperWrites[A <: AtomWrapper]
      (implicit companion: AtomCompanion[A], base: Writes[A#ValueType]): Writes[A] = Writes {
    (atom: A) => base.writes(atom.value)
  }

  implicit def atomWrapperFormats[A <: AtomWrapper]
      (implicit companion: AtomCompanion[A], r: Reads[A#ValueType], w: Writes[A#ValueType]): Format[A] = {
    Format(atomWrapperReads, atomWrapperWrites)
  }


}
object AtomWrapperFormats extends AtomWrapperFormats
