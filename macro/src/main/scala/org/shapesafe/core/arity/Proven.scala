package org.shapesafe.core.arity

trait Proven extends Arity with ProveArity.Proof {

//  final def in: this.type = this
}

object Proven {

  abstract class ProvenAs[O <: Leaf]()(implicit val out: O) extends Proven with ProveArity.Of[O] {}

}
