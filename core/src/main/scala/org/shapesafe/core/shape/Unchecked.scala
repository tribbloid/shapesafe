package org.shapesafe.core.shape

trait Unchecked extends LeafShape {

  override type Expr = "_UNCHECKED_"
}

object Unchecked extends Unchecked {}
