package org.shapesafe.core.axis

import org.shapesafe.core.Poly1Base
import org.shapesafe.core.arity.Arity

trait AxisMagnet {}

object AxisMagnet extends Poly1Base[AxisMagnet, Axis] {

  implicit def id[T <: Axis]: T ==> T = forAll[T].==>(identity)

  implicit def arity2Axis[T <: Arity] = forAll[T].==> { arity =>
    Axis.Nameless[T](arity)
  }
}
