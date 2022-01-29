package shapesafe.core.arity

trait Unprovable extends LeafArity {

  override type Expr = "_UNPROVABLE_"
}

case object Unprovable extends Unprovable {

  override def runtimeValue: Int =
    throw new UnsupportedOperationException(s"<${this.productPrefix}: no runtime value>")
}
