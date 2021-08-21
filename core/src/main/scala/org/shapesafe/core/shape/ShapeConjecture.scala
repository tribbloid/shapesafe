package org.shapesafe.core.shape

import org.shapesafe.core.debugging.CanRefute
import org.shapesafe.graph.commons.util.{ProductTree, TreeFormat}

trait ShapeConjecture extends Shape with CanRefute with ProductTree {

  override lazy val treeFormat: TreeFormat = TreeFormat.Indent2Minimal
//  final override def toString: String = treeString

}

object ShapeConjecture {}
