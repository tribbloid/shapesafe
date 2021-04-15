package org.shapesafe.core.shape

import com.tribbloids.graph.commons.util.TreeLike
import org.shapesafe.core.debugging.InfoCT
import org.shapesafe.core.debugging.InfoCT.CanRefute
import singleton.ops.+

trait ShapeConjecture extends Shape with CanRefute with TreeLike.ProductAsTree {

  final override def toString: String = treeString

  override type _Refute = InfoCT.noCanDo.T + InfoCT.nonExisting.T + _Peek
}
