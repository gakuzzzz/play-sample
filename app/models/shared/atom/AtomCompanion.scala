package models.shared.atom

import scalikejdbc._

trait AtomCompanion[A <: AtomWrapper] {

  def apply(value: A#ValueType): A

  implicit def companion: AtomCompanion[A] = this

  implicit def optionalTypeBinder(implicit ev: TypeBinder[Option[A#ValueType]]): TypeBinder[Option[A]] = ev.map(_.map(apply))

  implicit def typeBinder(implicit ev: TypeBinder[A#ValueType]): TypeBinder[A] = ev.map(apply)

}