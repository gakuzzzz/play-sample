package controllers.support.mappings

import play.api.data.format.Formatter
import play.api.data.{Mapping, FormError}
import play.api.data.Forms._
import models.shared.enum.{EnumCompanion, EnumLike}

trait EnumLikeMapping {

  implicit def enumLikeFormatter[A <: EnumLike]
  (implicit companion: EnumCompanion[A], base: Formatter[A#ValueType]): Formatter[A] = new Formatter[A] {
    override val format = base.format
    override def bind(key: String, data: Map[String, String]): Either[Seq[FormError], A] = {
      base.bind(key, data).right.flatMap(v => companion.valueOf(v).toRight(Seq(FormError(key, "error.enumerable"))))
    }
    override def unbind(key: String, value: A): Map[String, String] = base.unbind(key, value.value)
  }

  def enum[A <: EnumLike](implicit companion: EnumCompanion[A], base: Formatter[A#ValueType]): Mapping[A] = of[A]

}

object EnumLikeMapping extends EnumLikeMapping
