package org.shapesafe.core.arity.binary

import org.shapesafe.core.arity.Arity
import org.shapesafe.core.arity.ops.ArityOps.==!

trait Require2_Imp0 extends Require2_Imp1 {

  import org.shapesafe.core.arity.ProveArity._

  implicit def uncheckedEqual[
      A1 <: Arity,
      A2 <: Arity
  ](
      implicit
      domain: UncheckedDomain[A1, A2]
  ): (A1 ==! A2) =>> domain.Tightest = {
    domain.forEqual
  }
}
