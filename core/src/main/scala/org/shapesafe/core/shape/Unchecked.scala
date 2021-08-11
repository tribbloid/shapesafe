package org.shapesafe.core.shape

trait Unchecked extends LeafShape {

  override type _AsExpr = "_UNCHECKED_"
}

object Unchecked extends Unchecked {}
