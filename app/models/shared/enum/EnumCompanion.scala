package models.shared.enum

import scalikejdbc._

trait EnumCompanion[A <: EnumLike] {

  def values: IndexedSeq[A]

  def valueOf(value: A#ValueType): Option[A] = values.find(_.value == value)

  implicit def companion: EnumCompanion[A] = this

  implicit def optionTypeBinder(implicit ev: TypeBinder[Option[A#ValueType]]): TypeBinder[Option[A]] = ev.map(_.flatMap(valueOf))

  implicit def typeBinder(implicit ev: TypeBinder[A#ValueType]): TypeBinder[A] = ev.map(v => valueOf(v).get)

}
