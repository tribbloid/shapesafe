package shapesafe.core.arity

import shapesafe.core.debugging.ProofWithReasoning
import shapesafe.core.logic.ProofSystem

/**
  * Represents a reified Arity
  */
object ProveArity extends ProofSystem with ProofWithReasoning {

  object AsLeafArity extends Reasoner {

    type Goal = LeafArity
  }
}
