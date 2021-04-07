package org.shapesafe.core.arity

import com.tribbloids.graph.commons.util.TreeLike

trait ArityConjecture extends Arity.Verifiable with TreeLike.ProductAsTree {

//  final override def toString: String = treeString
}

object ArityConjecture {}
