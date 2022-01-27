package shapesafe.core.arity

object Unprovable extends ArityType {

  override def runtimeValue: Int = throw new UnsupportedOperationException(s"cannot verified an Unprovable")

  override type Expr = "_UNPROVABLE_"
}
