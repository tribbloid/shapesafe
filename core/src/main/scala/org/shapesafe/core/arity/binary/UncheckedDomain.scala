package org.shapesafe.core.arity.binary

import org.shapesafe.core.arity.UncheckedArity
import org.shapesafe.core.arity.ProveArity._
import org.shapesafe.core.arity.ops.ArityOps.==!
import org.shapesafe.core.arity.{Arity, ProveArity}

import scala.language.existentials

abstract class UncheckedDomain[
    A1 <: Arity,
    A2 <: Arity
] {

  import ProveArity.Factory._

  type O1 <: Arity

  def bound1: A1 |-< O1
  def bound2: A2 |-< Arity

  type Tightest <: Arity
  def selectTightest(a1: A1, a2: A2): Tightest

  def forOp2[OP <: Op2]: OP#On[A1, A2] =>> UncheckedArity =
    ProveArity.forAll[OP#On[A1, A2]].=>> { _ =>
      UncheckedArity
    }

  val forEqual: (A1 ==! A2) =>> Tightest =
    ProveArity.forAll[A1 ==! A2].=>> { v =>
      selectTightest(v.a1, v.a2)
    }

  def forRequire2[OP <: Require2]: OP#On[A1, A2] =>> O1 =
    ProveArity.forAll[OP#On[A1, A2]].=>> { v =>
      bound1.valueOf(v.a1)
    }
}

object UncheckedDomain extends UncheckedDomain_Imp0 {

  def summon[
      A1 <: Arity,
      A2 <: Arity,
      O <: Arity
  ](
      implicit
      self: UncheckedDomain[A1, A2]
  ): UncheckedDomain[A1, A2] = self

  case class D1[
      A1 <: Arity,
      A2 <: Arity,
      TC <: Arity
  ]()(
      implicit
      val bound1: A1 |-< TC,
      val bound2: A2 |-< UncheckedArity
  ) extends UncheckedDomain[A1, A2] {

    final type O1 = TC

    final type Tightest = TC
    override def selectTightest(a1: A1, a2: A2): Tightest = bound1.valueOf(a1)
  }

  implicit def d1[
      A1 <: Arity,
      A2 <: Arity,
      TC <: Arity
  ](
      implicit
      bound1: A1 |-< TC,
      bound2: A2 |-< UncheckedArity
  ): D1[A1, A2, TC] = D1()
}
