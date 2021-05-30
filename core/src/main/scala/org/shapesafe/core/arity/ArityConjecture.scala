package org.shapesafe.core.arity

import org.shapesafe.graph.commons.util.ProductTree
import org.shapesafe.core.debugging.DebugUtil.CanRefute

trait ArityConjecture extends Arity.Verifiable with CanRefute with ProductTree {

//  final override def toString: String = treeString
}

object ArityConjecture {}
