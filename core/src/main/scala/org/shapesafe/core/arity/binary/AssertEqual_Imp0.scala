package org.shapesafe.core.arity.binary

import org.shapesafe.core.arity.Arity
import org.shapesafe.core.arity.ProveArity.|-
import org.shapesafe.core.arity.ops.ArityOps.:==!

trait AssertEqual_Imp0 {

  implicit def unchecked[
      A1 <: Arity,
      A2 <: Arity,
      O <: Arity
  ](
      implicit
      domain: UncheckedDomain[A1, A2, O]
  ): (A1 :==! A2) |- O = {
    domain.forEqual
  }
}
