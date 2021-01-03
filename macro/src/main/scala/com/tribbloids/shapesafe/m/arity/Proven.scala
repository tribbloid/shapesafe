package com.tribbloids.shapesafe.m.arity

trait Proven extends Expression with OfArity.Proof {

  final def in: this.type = this
}

object Proven {

  abstract class ProvenToBe[O <: Arity]()(implicit val out: O) extends Proven with OfArity.Proof.Out_=[O] {}
}
