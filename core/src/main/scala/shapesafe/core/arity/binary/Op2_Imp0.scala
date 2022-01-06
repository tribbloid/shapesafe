package shapesafe.core.arity.binary

import shapesafe.core.arity._

trait Op2_Imp0 {

  implicit def unchecked[
      A1 <: Arity,
      A2 <: Arity,
      OP <: Op2
  ](
      implicit
      domain: UncheckedDomain[A1, A2]
  ) = {
    domain.forOp2[OP]
  }
}
