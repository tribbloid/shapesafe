package org.shapesafe.core.shape

import com.tribbloids.graph.commons.util.TreeLike

trait ShapeExpr extends Shape with TreeLike.ProductMixin {

  final override def toString: String = treeString
}
