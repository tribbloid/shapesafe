package org.shapesafe.core.arity

import org.shapesafe.core.ProofSystem

import scala.language.higherKinds

/**
  * Represents a reified Arity
  */
object ProveArity extends ProofSystem[Arity] {

  type OfUnknown = Proof.Lt[Leaf.Unknown]
  trait OfUnknownImpl extends Of[Leaf.Unknown]

  type OfLeaf = Proof.Lt[Leaf]
  trait OfStaticImpl[S] extends Of[Leaf.Static[S]] {

    type SS = S
  }
}
