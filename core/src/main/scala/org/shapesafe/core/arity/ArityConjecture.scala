package org.shapesafe.core.arity

import org.shapesafe.core.debugging.CanRefute
import org.shapesafe.graph.commons.util.ProductTree

trait ArityConjecture extends Arity.Verifiable with CanRefute with ProductTree {

//  final override def toString: String = treeString
}

object ArityConjecture {}
