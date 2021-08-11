package org.shapesafe.core.shape

trait Unchecked extends LeafShape {

//  override type _AsOpStr = "<Unchecked>"
  override type _AsExpr = "<Unchecked>"
}

object Unchecked extends Unchecked {}
