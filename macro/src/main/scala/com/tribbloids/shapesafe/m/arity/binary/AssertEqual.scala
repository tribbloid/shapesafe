package com.tribbloids.shapesafe.m.arity.binary

import com.tribbloids.shapesafe.m.arity.{Expression, ProveArity}
import com.tribbloids.shapesafe.m.arity.ProveArity.~~>
import singleton.ops.{==, Require}

case class AssertEqual[
    +A1 <: Expression,
    +A2 <: Expression
](
    a1: A1,
    a2: A2
) extends Expression {}

trait AssertEqual_Imp0 {

  implicit def unsafe[
      A1 <: Expression,
      A2 <: Expression,
      O <: ProveArity.Proof
  ](
      implicit
      domain: UnsafeDomain[A1, A2, O]
  ): UnsafeDomain[A1, A2, O]#ForEqual = {
    domain.ForEqual
  }
}

object AssertEqual extends AssertEqual_Imp0 {

  // TODO : both of these signatures are not refined enough, it should contains 4 cases:
  //  Const - Const -> Const[1]
  //  Const - Unsafe -> Const[1]
  //  Unsafe - Const -> Const[2]
  //  Unsafe - Unsafe -> Unsafe

  implicit def invar[
      A1 <: Expression,
      A2 <: Expression,
      S1,
      S2
  ](
      implicit
      bound1: A1 ~~> ProveArity.Invar[S1],
      bound2: A2 ~~> ProveArity.Invar[S2],
      lemma: Require[S1 == S2]
  ): InvarDomain[A1, A2, S1, S2]#ForEqual = {

    val domain = InvarDomain[A1, A2, S1, S2]()(bound1, bound2)

    domain.ForEqual()
  }
}
