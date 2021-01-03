package com.tribbloids.shapesafe.m.arity.binary

import com.tribbloids.shapesafe.m.arity.{Expression, OfArity}
import com.tribbloids.shapesafe.m.~~>
import singleton.ops.{==, Require}

case class AssertEqual[
    +A1 <: Expression,
    +A2 <: Expression
](
    a1: A1,
    a2: A2
) extends Expression {}

trait MayEqual_Imp0 {

  implicit def unsafe[
      A1 <: Expression,
      A2 <: Expression,
      O <: OfArity.Proof
  ](
      implicit
      domain: UnsafeDomain[A1, A2, O]
  ): AssertEqual[A1, A2] ~~> UnsafeDomain[A1, A2, O]#DynamicEqual = { v =>
    domain.DynamicEqual(v)
  }
}

object AssertEqual extends MayEqual_Imp0 {

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
      bound1: A1 ~~> OfArity.Invar[S1],
      bound2: A2 ~~> OfArity.Invar[S2],
      lemma: Require[S1 == S2]
  ): AssertEqual[A1, A2] ~~> InvarDomain[A1, A2, S1, S2]#Equal = {

    val domain = InvarDomain[A1, A2, S1, S2]()(bound1, bound2)

    { v =>
      domain.Equal(v)
    }
  }
}
