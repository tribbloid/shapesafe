package org.shapesafe.core.arity

object Unprovable extends Arity {

  override def runtimeValue: Int = throw new UnsupportedOperationException(s"cannot verified an Unprovable")

  override type _AsExpr = "<Unprovable>"
}
