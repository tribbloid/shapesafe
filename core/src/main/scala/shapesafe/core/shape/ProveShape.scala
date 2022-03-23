package shapesafe.core.shape

import shapesafe.core.debugging.ProofWithReasoning
import shapesafe.core.logic.ProofSystem

object ProveShape extends ProofSystem with ProofWithReasoning {

  object AsLeafShape extends Reasoner {

    type Goal = LeafShape
  }
}
