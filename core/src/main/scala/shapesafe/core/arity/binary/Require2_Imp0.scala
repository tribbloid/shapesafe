package shapesafe.core.arity.binary

import shapesafe.core.arity.ArityType
import shapesafe.core.Ops.==!

trait Require2_Imp0 extends Require2_Imp1 {

  import shapesafe.core.arity.ProveArity._

  implicit def uncheckedEqual[
      A1 <: ArityType,
      A2 <: ArityType
  ](
      implicit
      domain: UncheckedDomain[A1, A2]
  ): (A1 ==! A2) |- domain.Tightest = {
    domain.forEqual
  }
}
