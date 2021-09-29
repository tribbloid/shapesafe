package org.shapesafe.core

/**
  * This trait forms the backbone of compile-time reasoning and should reflect our best effort in reproducing
  * Curry-Howard isomorphism with scala compiler (regardless of how ill-suited it is), expecting drastic changes upon
  * improvement over its implicit search algorithm
  */
trait ProofSystem extends HasProposition with ProofScope { // TODO: no IUB?

  final type System = this.type
  final val system = this

  trait SubScopeInSystem extends ProofScope {

    final override type OUB = ProofSystem.this.OUB
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
