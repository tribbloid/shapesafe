package edu.umontreal.kotlingrad.shapesafe.m.arity.binary

import edu.umontreal.kotlingrad.shapesafe.m.arity.Utils.Op
import edu.umontreal.kotlingrad.shapesafe.m.arity.{Arity, Implies, Operand, Proof}

import scala.language.implicitConversions
import scala.language.higherKinds

case class Op2[
    +A1 <: Operand,
    +A2 <: Operand,
    ??[X1, X2] <: Op
](
    a1: A1,
    a2: A2
) extends Operand {}

object Op2 {

  implicit def proveInvar[
      A1 <: Operand,
      A2 <: Operand,
      S1,
      S2,
      ??[X1, X2] <: Op
  ](
      implicit
      bound1: A1 Implies Proof.Invar[S1],
      bound2: A2 Implies Proof.Invar[S2],
      lemma: S1 ?? S2
  ): Op2[A1, A2, ??] Implies Invars[A1, A2, S1, S2]#Op2Proof[??] = {
    val invars = Invars[A1, A2, S1, S2]()(bound1, bound2)

    v: Op2[A1, A2, ??] =>
      invars.Op2Proof[??](v)(lemma)

  }
}
