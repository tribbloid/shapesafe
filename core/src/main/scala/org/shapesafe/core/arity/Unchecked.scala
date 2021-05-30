package org.shapesafe.core.arity

trait Unchecked extends LeafArity {}

case object Unchecked extends Unchecked {
  override def runtimeArity: Int = throw new UnsupportedOperationException("<no runtime value>")
}
