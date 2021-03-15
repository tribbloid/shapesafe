package org.shapesafe.core.shape

import com.tribbloids.graph.commons.util.TreeLike

trait ShapeConjecture extends Shape with TreeLike.ProductAsTree {

  final override def toString: String = treeString
}
