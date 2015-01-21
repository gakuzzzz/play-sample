package models.shared.enum

trait EnumLike {

  type ValueType

  def value: ValueType

}
