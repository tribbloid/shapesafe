package edu.umontreal.kotlingrad.shapesafe.m.arity.binary

import edu.umontreal.kotlingrad.shapesafe.m.arity.Utils.Op
import edu.umontreal.kotlingrad.shapesafe.m.arity.{Arity, Operand, Proof}

import scala.language.higherKinds

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

  implicit class ProveInvar[A1 <: Operand, A2 <: Operand, Fr[X1, X2] <: Op](
      val self: Op2[A1, A2, Fr]
  )(
      implicit
      prove1: A1 => Proof.Invar,
      prove2: A2 => Proof.Invar
  ) extends Proof.Invar {

    val proof1: Proof.Invar = prove1(self.a1)
    val proof2: Proof.Invar = prove2(self.a2)

    val out1: proof1.Out = proof1.out
    val out2: proof2.Out = proof2.out

    override type Out = Arity.FromOp[out1.SS Fr out2.SS]

    override def out: Out = new Arity.FromOp[out1.SS Fr out2.SS](
      self.op(out1.number, out2.number)
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
