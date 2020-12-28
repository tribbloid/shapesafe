package com.tribbloids.shapesafe.m.arity

trait Proven extends Expression with ProofOfArity {

  final def self: this.type = this
}

object Proven {

  abstract class ProvenToBe[O <: Arity]()(implicit val out: O) extends Proven {

    type Out = O
  }
}
