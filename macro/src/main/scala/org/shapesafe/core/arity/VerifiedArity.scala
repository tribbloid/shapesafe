package org.shapesafe.core.arity

trait VerifiedArity extends Arity {

//  final def in: this.type = this
}

object VerifiedArity {

  import ProveArity.Factory._

  implicit def endo[T <: VerifiedArity]: T =>> T = ProveArity.forAll[T].=>>(identity[T])

//  abstract class ProvenAs[O <: LeafArity]()(
//      implicit
//      val out: O
//  ) extends Proven
//      with ProveArity.Of[O] {}

}
