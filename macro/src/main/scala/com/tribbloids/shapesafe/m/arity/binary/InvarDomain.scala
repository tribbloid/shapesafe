package com.tribbloids.shapesafe.m.arity.binary

import com.tribbloids.shapesafe.m.arity.Utils.Op
import com.tribbloids.shapesafe.m.arity.{Arity, Expression, ProofOfArity}
import com.tribbloids.shapesafe.m.~~>
import singleton.ops.{==, Require}

import scala.language.higherKinds

case class InvarDomain[
    A1 <: Expression,
    A2 <: Expression,
    S1,
    S2,
]()(
    implicit
    bound1: A1 ~~> ProofOfArity.Invar[S1],
    bound2: A2 ~~> ProofOfArity.Invar[S2],
) {

  case class Op2Proof[??[X1, X2] <: Op](
      self: Expr2[A1, A2, ??]
  )(
      implicit lemma: S1 ?? S2
  ) extends ProofOfArity.Invar[S1 ?? S2] {

    override type Out = Arity.FromOp[S1 ?? S2]

    override def out: Out = Arity.FromOp.summon[S1 ?? S2](lemma)
  }

  case class Equal(
      self: MayEqual[A1, A2]
  )(
      implicit lemma: Require[S1 == S2]
  ) extends ProofOfArity.Invar[S1] {

    val proof: ProofOfArity.Invar[S1] = bound1.apply(self.a1)

    override type Out = proof.Out

    override def out: Out = proof.out
  }
}
