package org.shapesafe.core.arity

import org.shapesafe.core.debugging.Expressions

object Unprovable extends Arity {
  override def runtimeArity: Int = throw new UnsupportedOperationException(s"cannot verified an Unprovable")

  override type _Expression = "???"
}
