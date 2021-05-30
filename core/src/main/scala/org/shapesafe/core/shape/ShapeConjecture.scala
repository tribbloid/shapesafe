package org.shapesafe.core.shape

import org.shapesafe.graph.commons.util.ProductTree
import org.shapesafe.core.debugging.DebugUtil.CanRefute

trait ShapeConjecture extends Shape with CanRefute with ProductTree {

  final override def toString: String = treeString

}

object ShapeConjecture {}
