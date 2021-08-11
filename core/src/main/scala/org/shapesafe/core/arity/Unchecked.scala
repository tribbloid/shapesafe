package org.shapesafe.core.arity

trait Unchecked extends LeafArity {

  override type _AsExpr = "<Unchecked>"
}

case object Unchecked extends Unchecked {
  override def runtimeValue: Int = throw new UnsupportedOperationException("<no runtime value>")
}
