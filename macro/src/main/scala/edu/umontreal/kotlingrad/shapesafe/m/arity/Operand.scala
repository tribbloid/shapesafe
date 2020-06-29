package edu.umontreal.kotlingrad.shapesafe.m.arity

import edu.umontreal.kotlingrad.shapesafe.m.arity.binary.Op2
import singleton.ops.{+, /}

// for "Expression"
trait Operand {

  def +[
      X >: this.type <: Operand,
      Y <: Operand
  ](that: Y): Op2[X, Y, +] = {

    Op2[X, Y, +](this, that)
  }

  def /[
      X >: this.type <: Operand,
      Y <: Operand
  ](that: Y): Op2[X, Y, /] = {

    Op2[X, Y, /](this, that)
  }

  final def asProof[
      T >: this.type <: Operand,
      R <: Proof
  ](implicit prove: T => R): R = prove(this)

  final def asGenericProof[
      T >: this.type
  ](
      implicit prove: T => Proof
  ): Proof = prove(this)
}

object Operand {

  abstract class ProvenToBe[O <: Arity]()(implicit val out: O) extends Operand {}

  object ProvenToBe {

    implicit class Trivial[O <: Arity, T <: ProvenToBe[O]](
        val self: T
    ) extends Proof {

      override type Out = O

      override def out: Out = self.out
    }
  }

//  object Proven {
//
//    // TODO: can this be simplified?
//    class ProofImpl[T <: Proven]() extends Proof[T] {
//
//      override type Out = T#Out
//
//      override def simplify(op: T): Out = op.out
//    }
//
//    implicit def prove[T <: Proven]: ProofImpl[T] = new ProofImpl[T]()
//  }
}
