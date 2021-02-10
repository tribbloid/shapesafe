package org.shapesafe.core.arity.binary

import org.shapesafe.core.arity.Utils.Op
import org.shapesafe.core.arity.{Arity, ProveArity}
import org.shapesafe.core.arity.ProveArity.~~>
import singleton.ops.impl.std
import singleton.twoface.impl.TwoFaceAny

import scala.language.{higherKinds, implicitConversions}

case class Op2[
    +A1 <: Arity,
    +A2 <: Arity,
    ??[X1, X2] <: Op
](
    a1: A1,
    a2: A2
)(
    implicit val tfs: TwoFaceAny.Int.Shell2[??, Int, std.Int, Int, std.Int]
) extends Arity {

  override lazy val number: Int = tfs.apply(a1.number, a2.number).getValue
}

trait Expr2_Imp0 {

  implicit def unsafe[
      A1 <: Arity,
      A2 <: Arity,
      O <: ProveArity.Proof,
      ??[X1, X2] <: Op
  ](
      implicit
      domain: UnknownDomain[A1, A2, O],
      tfs: TwoFaceAny.Int.Shell2[??, Int, std.Int, Int, std.Int]
  ): domain.ForOp2[??] = {
    domain.ForOp2[??]()
  }
}

object Op2 extends Expr2_Imp0 {

  implicit def invar[
      A1 <: Arity,
      A2 <: Arity,
      S1,
      S2,
      ??[X1, X2] <: Op
  ](
      implicit
      bound1: A1 ~~> ProveArity.OfStaticImpl[S1], // TODO: make it similar to unsafe
      bound2: A2 ~~> ProveArity.OfStaticImpl[S2],
      lemma: S1 ?? S2
  ): InvarDomain[A1, A2, S1, S2]#ForOp2[??] = {
    val domain = InvarDomain[A1, A2, S1, S2]()(bound1, bound2)

    domain.ForOp2[??]()(lemma)
  }
}
