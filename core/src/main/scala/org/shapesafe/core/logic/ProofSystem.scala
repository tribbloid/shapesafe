package org.shapesafe.core.logic

/**
  * This trait forms the backbone of compile-time reasoning and should reflect our best effort in reproducing
  * Curry-Howard isomorphism with scala compiler (regardless of how ill-suited it is), expecting drastic changes upon
  * improvement over its implicit search algorithm
  */
trait ProofSystem extends HasProposition with AxiomSet { // TODO: no IUB?

  final type System = this.type
  final val system = this

  trait Bound

  trait ExtensionLike extends AxiomSet {

    final override type OUB = ProofSystem.this.OUB

//    override type Bound <: ProofSystem.this.ExtensionBound

    final override type System = ProofSystem.this.type
    final override val system = ProofSystem.this
  }
}

object ProofSystem {

  type Aux[T] = ProofSystem {
    type OUB = T
  }

  trait ^[T] extends ProofSystem {
    final override type OUB = T
  }
}
