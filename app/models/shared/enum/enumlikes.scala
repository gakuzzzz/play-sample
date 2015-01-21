package models.shared.enum

trait StringEnumLike extends EnumLike {

  type ValueType = String

  override def toString = value

}
trait ShortEnumLike extends EnumLike {

  type ValueType = Short

}
