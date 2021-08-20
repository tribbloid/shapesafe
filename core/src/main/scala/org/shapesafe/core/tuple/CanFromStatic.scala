package org.shapesafe.core.tuple

trait CanFromStatic extends CanCons {
  _self: TupleSystem =>

  object FromStatic extends ConsIntake[VBound] {}
}
