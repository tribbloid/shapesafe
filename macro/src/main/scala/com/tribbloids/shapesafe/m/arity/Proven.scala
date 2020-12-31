package com.tribbloids.shapesafe.m.arity

trait Proven extends Expression with ProofOfArity {

  final def in: this.type = this
}

object Proven {

  abstract class ProvenToBe[O <: Arity]()(implicit val out: O) extends Proven with ProofOfArity.Out_=[O] {}
}
