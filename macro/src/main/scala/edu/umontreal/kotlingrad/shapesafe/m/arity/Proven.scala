package edu.umontreal.kotlingrad.shapesafe.m.arity

trait Proven extends Operand with Proof {

  final def self: this.type = this
}

object Proven {

  abstract class ProvenToBe[O <: Arity]()(implicit val out: O) extends Proven {

    type Out = O
  }

  implicit def trivial[T <: Proven]: T Implies T = identity
}
