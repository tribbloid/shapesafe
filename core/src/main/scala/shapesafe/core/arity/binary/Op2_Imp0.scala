package shapesafe.core.arity.binary

import shapesafe.core.arity.ProveArity.|-
import shapesafe.core.arity._

trait Op2_Imp0 extends Op2Like_Imp0 {

  implicit def unchecked[
      A1 <: ArityType,
      A2 <: ArityType,
      OP <: Op2
  ](
      implicit
      domain: UncheckedDomain[A1, A2]
  ): OP#On[A1, A2] |- Unchecked = {
    domain.forOp2[OP]
  }
}
