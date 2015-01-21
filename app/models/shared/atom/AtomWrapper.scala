package models.shared.atom

trait AtomWrapper {

  type ValueType

  def value: ValueType

  override def toString: String = value.toString

}
