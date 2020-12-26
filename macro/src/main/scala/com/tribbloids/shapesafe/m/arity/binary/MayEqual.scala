package com.tribbloids.shapesafe.m.arity.binary

import com.tribbloids.shapesafe.m.arity.{Operand, Proof}
import com.tribbloids.shapesafe.m.~~>
import singleton.ops.{==, Require}

case class MayEqual[
    +A1 <: Operand,
    +A2 <: Operand
](
    a1: A1,
    a2: A2
) extends Operand {}

trait MayEqual_Imp0 {

  implicit def unsafe[
      A1 <: Operand,
      A2 <: Operand,
      O <: Proof
  ](
      implicit
      domain: UnsafeDomain[A1, A2, O]
  ): MayEqual[A1, A2] ~~> UnsafeDomain[A1, A2, O]#ProbablyEqual = { v =>
    domain.ProbablyEqual(v)
  }
}

object MayEqual extends MayEqual_Imp0 {

  // TODO : both of these signatures are not refined enough, it should contains 4 cases:
  //  Const - Const -> Const[1]
  //  Const - Unsafe -> Const[1]
  //  Unsafe - Const -> Const[2]
  //  Unsafe - Unsafe -> Unsafe

  implicit def invar[
      A1 <: Operand,
      A2 <: Operand,
      S1,
      S2
  ](
      implicit
      bound1: A1 ~~> Proof.Invar[S1],
      bound2: A2 ~~> Proof.Invar[S2],
      lemma: Require[S1 == S2]
  ): MayEqual[A1, A2] ~~> InvarDomain[A1, A2, S1, S2]#Equal = {

    val domain = InvarDomain[A1, A2, S1, S2]()(bound1, bound2)

    { v =>
      domain.Equal(v)
    }
  }
}
