package org.shapesafe.core.arity

import com.tribbloids.graph.commons.util.TreeLike
import org.shapesafe.core.debugging.DebuggingUtil.CanRefute

trait ArityConjecture extends Arity.Verifiable with CanRefute with TreeLike.ProductAsTree {

//  final override def toString: String = treeString
}

object ArityConjecture {}
