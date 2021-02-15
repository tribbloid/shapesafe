package org.shapesafe.core.arity

import ProveArity.=>>

trait Proven extends Arity {

//  final def in: this.type = this
}

object Proven {

  implicit def trivial[T <: Proven]: T =>> T = ProveArity.from[T].out(identity[T])

//  abstract class ProvenAs[O <: LeafArity]()(
//      implicit
//      val out: O
//  ) extends Proven
//      with ProveArity.Of[O] {}

}
