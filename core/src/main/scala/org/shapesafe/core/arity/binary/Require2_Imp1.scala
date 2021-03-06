package org.shapesafe.core.arity.binary

import org.shapesafe.core.arity.Arity
import org.shapesafe.core.arity.ProveArity.|-

trait Require2_Imp1 {

  implicit def unchecked[
      A1 <: Arity,
      A2 <: Arity,
      O <: Arity,
      OP <: Require2
  ](
      implicit
      domain: UncheckedDomain[A1, A2]
  ): OP#On[A1, A2] |- domain.O1 = {
    domain.forRequire2[OP]
  }
}
