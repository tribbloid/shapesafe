package shapesafe.core.shape

import shapesafe.core.debugging.CanRefute

trait ShapeConjecture extends ShapeType with CanRefute {

//  override lazy val treeFormat = TreeFormat.Indent2Minimal
//  final override def toString: String = treeString

}

object ShapeConjecture {}
