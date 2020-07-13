package edu.umontreal.kotlingrad.shapesafe.m.arity.binary

import edu.umontreal.kotlingrad.shapesafe.m.arity.Utils.Op
import edu.umontreal.kotlingrad.shapesafe.m.arity.{Operand, Proof}
import edu.umontreal.kotlingrad.shapesafe.m.~~>
import singleton.ops.impl.std
import singleton.twoface.impl.TwoFaceAny

import scala.language.{higherKinds, implicitConversions}

case class Op2[
    +A1 <: Operand,
    +A2 <: Operand,
    ??[X1, X2] <: Op
](
    a1: A1,
    a2: A2
) extends Operand {}

trait Op2_Imp0 {

  implicit def unsafe[
      A1 <: Operand,
      A2 <: Operand,
      O <: Proof,
      ??[X1, X2] <: Op
  ](
      implicit
      domain: UnsafeDomain[A1, A2, O],
      tfs: TwoFaceAny.Int.Shell2[??, Int, std.Int, Int, std.Int]
  ): Op2[A1, A2, ??] ~~> Proof.Unsafe = { v =>
    domain.Op2Proof(v)
  }
}

object Op2 extends Op2_Imp0 {

  implicit def invar[
      A1 <: Operand,
      A2 <: Operand,
      S1,
      S2,
      ??[X1, X2] <: Op
  ](
      implicit
      bound1: A1 ~~> Proof.Invar[S1],
      bound2: A2 ~~> Proof.Invar[S2],
      lemma: S1 ?? S2
  ): Op2[A1, A2, ??] ~~> InvarDomain[A1, A2, S1, S2]#Op2Proof[??] = {
    val domain = InvarDomain[A1, A2, S1, S2]()(bound1, bound2)

    { v =>
      domain.Op2Proof[??](v)(lemma)
    }
  }
}
