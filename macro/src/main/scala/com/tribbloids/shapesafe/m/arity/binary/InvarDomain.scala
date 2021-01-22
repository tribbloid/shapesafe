package com.tribbloids.shapesafe.m.arity.binary

import com.tribbloids.shapesafe.m.arity.Utils.Op
import com.tribbloids.shapesafe.m.arity.{Arity, Expression}
import com.tribbloids.shapesafe.m.arity.ProveArity._
import singleton.ops.{==, Require}

import scala.language.higherKinds

case class InvarDomain[
    A1 <: Expression,
    A2 <: Expression,
    S1,
    S2,
]()(
    implicit
    bound1: A1 ~~> Invar[S1],
    bound2: A2 ~~> Invar[S2],
) {

  case class ForOp2[??[X1, X2] <: Op]()(
      implicit lemma: S1 ?? S2
  ) extends (Expr2[A1, A2, ??] ForAll Invar[S1 ?? S2]) {

    case class prove(in: Expr2[A1, A2, ??]) extends Invar[S1 ?? S2] {

      override def out: Out = Arity.FromOp.summon[S1 ?? S2](lemma)
    }
  }

  case class ForEqual()(
      implicit lemma: Require[S1 == S2]
  ) extends (AssertEqual[A1, A2] ~~> Invar[S1]) {

    def apply(in: AssertEqual[A1, A2]): Invar[S1] = bound1.apply(in.a1)
  }
}
