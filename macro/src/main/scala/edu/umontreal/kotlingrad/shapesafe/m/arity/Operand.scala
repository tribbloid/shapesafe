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
  ](implicit prove: T Implies R): R = prove.transform(this)

  final def asGenericProof[
      T >: this.type <: Operand
  ](
      implicit prove: T Implies Proof
  ): Proof = prove.transform(this)
}

object Operand {

  abstract class ProvenToBe[O <: Arity]()(implicit val out: O) extends Operand {}

  object ProvenToBe {

    class Trivial[O <: Arity](
        val self: ProvenToBe[O]
    ) extends Proof {

      override type Out = O

      override def out: Out = self.out
    }

    implicit def trivial[O <: Arity]: ProvenToBe[O] Implies Trivial[O] = v => new Trivial[O](v)
  }
}
