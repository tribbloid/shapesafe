package shapesafe.core.shape

import shapesafe.core.debugging.CanRefute
import ai.acyclic.graph.commons.{ProductTree, TreeFormat}

trait ShapeConjecture extends ShapeType with CanRefute with ProductTree {

  override lazy val treeFormat: TreeFormat = TreeFormat.Indent2Minimal
//  final override def toString: String = treeString

}

object ShapeConjecture {}
