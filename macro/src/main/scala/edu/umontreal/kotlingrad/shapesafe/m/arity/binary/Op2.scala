package edu.umontreal.kotlingrad.shapesafe.m.arity.binary

import edu.umontreal.kotlingrad.shapesafe.m.arity.Utils.Op
import edu.umontreal.kotlingrad.shapesafe.m.arity.{Arity, Operand, Proof}

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

  implicit class ProveInvar[
      A1 <: Operand,
      A2 <: Operand,
      S1,
      S2,
      ??[X1, X2] <: Op
  ](
      val self: Op2[A1, A2, ??]
  )(
      implicit
      bound1: A1 => Proof.Invar[S1],
      bound2: A2 => Proof.Invar[S2],
      lemma: S1 ?? S2
  ) extends Proof.Invar[S1 ?? S2] {

    override type Out = Arity.FromOp[S1 ?? S2]

    override def out: Out = new Arity.FromOp[S1 ?? S2](
      lemma.value.asInstanceOf[Int]
    )
  }

//  implicit class ProveUnsafe[A1 <: Operand, A2 <: Operand, Fr[X1, X2] <: Op](
//      val self: Op2[A1, A2, Fr]
//  )(
//      implicit
//      prove1: A1 => Proof,
//      prove2: A2 => Proof
//  ) extends Proof.Unsafe {}

}
