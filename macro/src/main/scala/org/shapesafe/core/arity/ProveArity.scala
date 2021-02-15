package org.shapesafe.core.arity

import org.shapesafe.core.ProofSystem

import scala.language.higherKinds

/**
  * Represents a reified Arity
  */
object ProveArity extends ProofSystem[Arity] {

  trait OfUnknown extends Of[LeafArity.Unknown]

  trait OfStatic[S] extends Of[LeafArity.Static[S]] {
    type SS = S
  }
}
