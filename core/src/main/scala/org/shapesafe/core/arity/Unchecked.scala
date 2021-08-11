package org.shapesafe.core.arity

trait Unchecked extends LeafArity {

  override type _AsExpr = "_UNCHECKED_"
}

case object Unchecked extends Unchecked {
  override def runtimeValue: Int = throw new UnsupportedOperationException("<no runtime value>")
}
