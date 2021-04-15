package org.shapesafe.core.shape

import com.tribbloids.graph.commons.util.TreeLike
import org.shapesafe.core.debugging.InfoCT
import singleton.ops.+

trait ShapeConjecture extends Shape with TreeLike.ProductAsTree {

  final override def toString: String = treeString

}

object ShapeConjecture {}
