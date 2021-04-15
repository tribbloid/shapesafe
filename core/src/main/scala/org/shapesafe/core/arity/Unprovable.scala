package org.shapesafe.core.arity

object Unprovable extends Arity {
  override def runtimeArity: Int = throw new UnsupportedOperationException(s"cannot verified an Unprovable")
}
