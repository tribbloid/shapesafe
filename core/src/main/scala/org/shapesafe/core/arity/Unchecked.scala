package org.shapesafe.core.arity

trait Unchecked extends LeafArity {

  override type Expr = "_UNCHECKED_"
}

case object Unchecked extends Unchecked {
  override def runtimeValue: Int = throw new UnsupportedOperationException("<no runtime value>")
}
