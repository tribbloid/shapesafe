package edu.umontreal.kotlingrad.shapesafe.m.arity.binary

import edu.umontreal.kotlingrad.shapesafe.m.arity.Utils.Op
import edu.umontreal.kotlingrad.shapesafe.m.arity.{Arity, Operand, Proof}

import scala.language.{higherKinds, implicitConversions}

case class Op2[
    +A1 <: Operand,
    +A2 <: Operand,
    Fr[X1, X2] <: Op
](
    a1: A1,
    a2: A2,
    op: (Int, Int) => Int
) extends Operand {}

object Op2 {

  implicit class ProveInvar[
      A1 <: Operand,
      A2 <: Operand,
      S1,
      S2,
      Fr[X1, X2] <: Op
  ](
      override val in: Op2[A1, A2, Fr]
  )(
      implicit
      val prove1: A1 => Proof.Invar[S1],
      val prove2: A2 => Proof.Invar[S2],
      val ss: S1 Fr S2
  ) extends Proof.Invar[S1 Fr S2]
      with Proof.From[Op2[A1, A2, Fr]] {

//    val proof1: Proof.Invar[S1] = prove1(in.a1)
//    val proof2: Proof.Invar[S2] = prove2(in.a2)
//
//    val out1: proof1.Out = proof1.out
//    val out2: proof2.Out = proof2.out

    override type Out = Arity.FromOp[S1 Fr S2]

    override val out: Out = new Arity.FromOp[S1 Fr S2]()
  }

//  implicit def ProveInvar[
//      A1 <: Operand,
//      A2 <: Operand,
//      S1,
//      S2,
//      Fr[X1, X2] <: Op
//  ](
//      in: Op2[A1, A2, Fr]
//  )(
//      implicit
//      prove1: A1 => Proof.Invar[S1],
//      prove2: A2 => Proof.Invar[S2],
////      ss: Lazy[S1 Fr S2]
//  ): Proof.Invar[S1 Fr S2] = {
//
////    implicit val _ss: Fr[S1, S2] = ss.value
//
//    new ProveInvar(in)
//  }

//  implicit class ProveUnsafe[A1 <: Operand, A2 <: Operand, Fr[X1, X2] <: Op](
//      val self: Op2[A1, A2, Fr]
//  )(
//      implicit
//      prove1: A1 => Proof,
//      prove2: A2 => Proof
//  ) extends Proof.Unsafe {}

}
