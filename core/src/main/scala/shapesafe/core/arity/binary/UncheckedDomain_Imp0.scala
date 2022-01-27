package shapesafe.core.arity.binary

import shapesafe.core.arity.ArityType
import shapesafe.core.arity.Unchecked
import shapesafe.core.arity.ProveArity._

trait UncheckedDomain_Imp0 {

  case class D2[
      A1 <: ArityType,
      A2 <: ArityType,
      TC <: ArityType
  ]()(
      implicit
      val bound1: A1 |-< Unchecked,
      val bound2: A2 |-< TC
  ) extends UncheckedDomain[A1, A2] {

    final type O1 = Unchecked

    final type Tightest = TC
    override def selectTightest(a1: A1, a2: A2): Tightest = bound2.instanceFor(a2)
  }

  implicit def d2[
      A1 <: ArityType,
      A2 <: ArityType,
      TC <: ArityType
  ](
      implicit
      bound1: A1 |-< Unchecked,
      bound2: A2 |-< TC
  ): D2[A1, A2, TC] = D2()(bound1, bound2)
}
