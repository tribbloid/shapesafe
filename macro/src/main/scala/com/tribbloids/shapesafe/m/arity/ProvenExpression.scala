package com.tribbloids.shapesafe.m.arity

trait ProvenExpression extends Expression with OfArity.Proof {

//  final def in: this.type = this
}

object ProvenExpression {

  abstract class ProvenToBe[O <: Arity]()(implicit val out: O) extends ProvenExpression with OfArity.Out_=[O] {}
}
