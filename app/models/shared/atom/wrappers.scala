package models.shared.atom


trait StringAtomWrapper extends AtomWrapper {
  type ValueType = String
}
trait IntAtomWrapper extends AtomWrapper {
  type ValueType = Int
}
trait LongAtomWrapper extends AtomWrapper {
  type ValueType = Long
}