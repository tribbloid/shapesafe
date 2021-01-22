package com.tribbloids.shapesafe.m.arity.binary

import com.tribbloids.shapesafe.m.arity.Utils.Op
import com.tribbloids.shapesafe.m.arity.{Expression, ProveArity}
import com.tribbloids.shapesafe.m.arity.ProveArity.~~>
import singleton.ops.impl.std
import singleton.twoface.impl.TwoFaceAny

import scala.language.{higherKinds, implicitConversions}

case class Expr2[
    +A1 <: Expression,
    +A2 <: Expression,
    ??[X1, X2] <: Op
](
    a1: A1,
    a2: A2
) extends Expression {}

trait Expr2_Imp0 {

  implicit def unsafe[
      A1 <: Expression,
      A2 <: Expression,
      O <: ProveArity.Proof,
      ??[X1, X2] <: Op
  ](
      implicit
      domain: UnsafeDomain[A1, A2, O],
      tfs: TwoFaceAny.Int.Shell2[??, Int, std.Int, Int, std.Int]
  ): domain.ForOp2[??] = {
    domain.ForOp2[??]()
  }
}

object Expr2 extends Expr2_Imp0 {

  implicit def invar[
      A1 <: Expression,
      A2 <: Expression,
      S1,
      S2,
      ??[X1, X2] <: Op
  ](
      implicit
      bound1: A1 ~~> ProveArity.Invar[S1],
      bound2: A2 ~~> ProveArity.Invar[S2],
      lemma: S1 ?? S2
  ): InvarDomain[A1, A2, S1, S2]#ForOp2[??] = {
    val domain = InvarDomain[A1, A2, S1, S2]()(bound1, bound2)

    domain.ForOp2[??]()(lemma)
  }
}
