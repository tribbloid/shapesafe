package org.shapesafe.m.arity

trait ProvenExpression extends Expression with ProveArity.Proof {

//  final def in: this.type = this
}

object ProvenExpression {

  abstract class ProvenToBe[O <: Arity]()(implicit val out: O) extends ProvenExpression with ProveArity.Out_=[O] {}
}
