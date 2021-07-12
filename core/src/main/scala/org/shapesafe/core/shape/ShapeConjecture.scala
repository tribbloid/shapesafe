package org.shapesafe.core.shape

import org.shapesafe.graph.commons.util.{ProductTree, TreeFormat}
import org.shapesafe.core.debugging.DebugUtil.CanRefute

trait ShapeConjecture extends Shape with CanRefute with ProductTree {

  override lazy val treeFormat: TreeFormat = TreeFormat.Indent2Minimal
//  final override def toString: String = treeString

}

object ShapeConjecture {}
