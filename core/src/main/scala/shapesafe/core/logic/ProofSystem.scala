package shapesafe.core.logic

/**
  * This trait forms the backbone of compile-time reasoning and should reflect our best effort in reproducing
  * Curry-Howard isomorphism with scala compiler (regardless of how ill-suited it is), expecting drastic changes upon
  * improvement over its implicit search algorithm
  */
trait ProofSystem extends HasProposition with Theory { // TODO: no IUB?

  final type System = this.type
  final val system = this

  trait Bound

  trait ExtensionLike extends Theory {

//    override type Bound <: ProofSystem.this.ExtensionBound

    final override type System = ProofSystem.this.type
    final override val system = ProofSystem.this
  }
}

object ProofSystem {}
