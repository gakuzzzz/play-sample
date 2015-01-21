package controllers.support.mappings

import play.api.data.Forms._
import play.api.data.format.Formatter
import models.shared.atom.{AtomWrapper, AtomCompanion}
import play.api.data.{Mapping, FormError}

trait AtomWrapperMapping {

  implicit def atomWrapperFormatter[A <: AtomWrapper]
      (implicit companion: AtomCompanion[A], base: Formatter[A#ValueType]): Formatter[A] = new Formatter[A] {
    override val format = base.format
    override def bind(key: String, data: Map[String, String]): Either[Seq[FormError], A] = {
      base.bind(key, data).right.map(companion.apply)
    }
    override def unbind(key: String, value: A): Map[String, String] = base.unbind(key, value.value)
  }

  def atom[A <: AtomWrapper](implicit companion: AtomCompanion[A], base: Formatter[A#ValueType]): Mapping[A] = of[A]

}

object AtomWrapperMapping extends AtomWrapperMapping
