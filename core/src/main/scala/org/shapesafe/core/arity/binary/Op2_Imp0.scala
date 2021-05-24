package org.shapesafe.core.arity.binary

import org.shapesafe.core.arity.Utils.Op
import org.shapesafe.core.arity._

import scala.language.implicitConversions

trait Op2_Imp0 {

  implicit def unchecked[
      A1 <: Arity,
      A2 <: Arity,
      O <: Arity,
      ??[X1, X2] <: Op
  ](
      implicit
      domain: UncheckedDomain[A1, A2, O],
      sh: Utils.IntSh[??]
  ) = {
    domain.forOp2[??]
  }
}
