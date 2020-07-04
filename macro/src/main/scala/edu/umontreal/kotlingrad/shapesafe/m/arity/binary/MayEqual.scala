package edu.umontreal.kotlingrad.shapesafe.m.arity.binary

import edu.umontreal.kotlingrad.shapesafe.m.arity.{Implies, Operand, Proof}
import singleton.ops.{==, Require}

case class MayEqual[
    +A1 <: Operand,
    +A2 <: Operand
](
    a1: A1,
    a2: A2
) extends Operand {}

object MayEqual {

  // TODO : both of these signatures are not refined enough, it should contains 4 cases:
  //  Const - Const -> Const[1]
  //  Const - Unsafe -> Const[1]
  //  Unsafe - Const -> Const[2]
  //  Unsafe - Unsafe -> Unsafe

  implicit def proveInvar[
      A1 <: Operand,
      A2 <: Operand,
      S1,
      S2
  ](
      implicit
      bound1: A1 Implies Proof.Invar[S1],
      bound2: A2 Implies Proof.Invar[S2],
      lemma: Require[S1 == S2]
  ): MayEqual[A1, A2] Implies Invars[A1, A2, S1, S2]#EqualProof = {

    val invars = Invars[A1, A2, S1, S2]()(bound1, bound2)

    v =>
      invars.EqualProof(v)
  }
}
