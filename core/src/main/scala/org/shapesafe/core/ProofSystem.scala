package org.shapesafe.core

/**
  * This trait forms the backbone of compile-time reasoning and should reflect our best effort in reproducing
  * Curry-Howard isomorphism with scala compiler (regardless of how ill-suited it is), expecting drastic changes upon
  * improvement over its implicit search algorithm
  */
trait ProofSystem extends HasProposition with ProofScope { // TODO: no IUB?

  final type System = this.type
  final val system = this

  trait UpcastEvidence

  trait ScopeInSystem extends ProofScope {

    final override type OUB = ProofSystem.this.OUB

//    final override type System = ProofSystem.this.System
//    final override val system = ProofSystem.this.system

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
