package org.shapesafe.core.arity

trait UncheckedArity extends LeafArity {}

case object UncheckedArity extends UncheckedArity {
  override def runtimeArity: Int = throw new UnsupportedOperationException("<no runtime value>")
}
