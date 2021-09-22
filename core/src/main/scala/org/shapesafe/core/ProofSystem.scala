package org.shapesafe.core

import scala.language.implicitConversions

/**
  * This trait forms the backbone of compile-time reasoning and should reflect our best effort in reproducing
  * Curry-Howard isomorphism with scala compiler (regardless of how ill-suited it is), expecting drastic changes upon
  * improvement over its implicit search algorithm
  */
trait ProofSystem extends HasPropositions with ProofScope { // TODO: no IUB?

  final val system: this.type = this

  implicit def coerciveUpcast[I, P <: Proposition, S <: SameSystemScope](
      implicit
      proofInSubScope: S#SubScope#Proof[I, P]
  ): S#Proof[I, P] = { (v: I) =>
    proofInSubScope.consequentFor(v)
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
