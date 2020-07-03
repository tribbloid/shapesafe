package edu.umontreal.kotlingrad.shapesafe.m.arity

import edu.umontreal.kotlingrad.shapesafe.m.arity.binary.Op2
import singleton.ops._

// for "Expression"
trait Operand {

  def +[
      X >: this.type <: Operand,
      Y <: Operand
  ](that: Y): Op2[X, Y, +] = {

    Op2[X, Y, +](this, that)
  }

  def -[
      X >: this.type <: Operand,
      Y <: Operand
  ](that: Y): Op2[X, Y, -] = {

    Op2[X, Y, -](this, that)
  }

  def *[
      X >: this.type <: Operand,
      Y <: Operand
  ](that: Y): Op2[X, Y, *] = {

    Op2[X, Y, *](this, that)
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
  ](implicit prove: T Implies R): R = prove.apply(this)

  final def asGenericProof[
      T >: this.type <: Operand
  ](
      implicit prove: T Implies Proof
  ): Proof = prove.apply(this)
}

object Operand {

  abstract class ProvenToBe[O <: Arity]()(implicit val out: O) extends Operand {}

  object ProvenToBe {

    case class Trivial[O <: Arity](
        self: ProvenToBe[O]
    ) extends Proof {

      override type Out = O

      override def out: Out = self.out
    }

    implicit def trivial[O <: Arity]: ProvenToBe[O] Implies Trivial[O] = v => Trivial[O](v)
  }
}
