package org.shapesafe.core.tuple

trait CanFromLiterals extends CanCons {
  _self: TupleSystem =>

  object FromLiterals extends ConsIntake[VBound with Singleton] {}
}
