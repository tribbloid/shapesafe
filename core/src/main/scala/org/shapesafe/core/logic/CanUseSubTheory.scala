package org.shapesafe.core.logic

trait CanUseSubTheory {
  self: Theory =>

  object SubTheory {
    type Aux = System#ExtensionLike {

      type ExtensionBound >: self.Bound
    }
  }

  implicit def coerciveUpcast[
      I,
      P <: Consequent
  ](
      implicit
      proofInSubTheory: SubTheory.Aux#Proof[I, P]
      // proof defined for SubTheory can be used directly
  ): Proof[I, P] = { (v: I) =>
    proofInSubTheory.consequentFor(v)
  }
}
