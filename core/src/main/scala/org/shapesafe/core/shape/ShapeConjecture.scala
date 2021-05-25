package org.shapesafe.core.shape

import com.tribbloids.graph.commons.util.TreeLike
import org.shapesafe.core.debugging.DebugUtil.CanRefute

trait ShapeConjecture extends Shape with CanRefute with TreeLike.ProductAsTree {

  final override def toString: String = treeString

}

object ShapeConjecture {}
