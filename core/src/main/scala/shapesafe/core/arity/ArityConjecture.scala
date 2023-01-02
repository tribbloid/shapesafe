package shapesafe.core.arity

import shapesafe.core.debugging.CanRefute

trait ArityConjecture extends ArityType.Verifiable with CanRefute {

//  final override def toString: String = treeString
}

object ArityConjecture {}
