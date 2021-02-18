package org.shapesafe.core.arity

import ProveArity.=>>

trait VerifiedArity extends Arity {

//  final def in: this.type = this
}

object VerifiedArity {

  implicit def endo[T <: VerifiedArity]: T =>> T = ProveArity.from[T].=>>(identity[T])

//  abstract class ProvenAs[O <: LeafArity]()(
//      implicit
//      val out: O
//  ) extends Proven
//      with ProveArity.Of[O] {}

}
