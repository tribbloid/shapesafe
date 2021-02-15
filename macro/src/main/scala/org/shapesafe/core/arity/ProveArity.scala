package org.shapesafe.core.arity

import org.shapesafe.core.ProofSystem

import scala.language.higherKinds

/**
  * Represents a reified Arity
  */
object ProveArity extends ProofSystem[Arity] {

  trait OfUnchecked extends Of[LeafArity.Unchecked]

  trait OfStatic[S] extends Of[LeafArity.Const[S]] {
    type SS = S
  }
}
