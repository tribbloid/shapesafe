package shapesafe.core.arity.binary

import shapesafe.core.arity.Unchecked
import shapesafe.core.arity.ProveArity._
import shapesafe.core.Ops.==!
import shapesafe.core.arity.{ArityType, ProveArity}

abstract class UncheckedDomain[
    A1 <: ArityType,
    A2 <: ArityType
] {

  import ProveArity._

  type O1 <: ArityType

  def bound1: A1 |-< O1
  def bound2: A2 |-< ArityType

  type Tightest <: ArityType
  def selectTightest(a1: A1, a2: A2): Tightest

  def forOp2[OP <: Op2]: OP#On[A1, A2] |- Unchecked =
    ProveArity.forAll[OP#On[A1, A2]].=>> { _ =>
      Unchecked
    }

  val forEqual: (A1 ==! A2) |- Tightest =
    ProveArity.forAll[A1 ==! A2].=>> { v =>
      selectTightest(v.a1, v.a2)
    }

  def forRequire2[OP <: Require2]: OP#On[A1, A2] |- O1 =
    ProveArity.forAll[OP#On[A1, A2]].=>> { v =>
      bound1.instanceFor(v.a1)
    }
}

object UncheckedDomain extends UncheckedDomain_Imp0 {

  def summon[
      A1 <: ArityType,
      A2 <: ArityType,
      O <: ArityType
  ](
      implicit
      self: UncheckedDomain[A1, A2]
  ): UncheckedDomain[A1, A2] = self

  case class D1[
      A1 <: ArityType,
      A2 <: ArityType,
      TC <: ArityType
  ]()(
      implicit
      val bound1: A1 |-< TC,
      val bound2: A2 |-< Unchecked
  ) extends UncheckedDomain[A1, A2] {

    final type O1 = TC

    final type Tightest = TC
    override def selectTightest(a1: A1, a2: A2): Tightest = bound1.instanceFor(a1)
  }

  implicit def d1[
      A1 <: ArityType,
      A2 <: ArityType,
      TC <: ArityType
  ](
      implicit
      bound1: A1 |-< TC,
      bound2: A2 |-< Unchecked
  ): D1[A1, A2, TC] = D1()
}
