package shapesafe.core.arity

import shapesafe.core.debugging.CanRefute
import ai.acyclic.prover.commons.ProductTree

trait ArityConjecture extends ArityType.Verifiable with CanRefute with ProductTree {

//  final override def toString: String = treeString
}

object ArityConjecture {}
