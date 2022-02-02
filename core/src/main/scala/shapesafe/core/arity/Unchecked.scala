package shapesafe.core.arity

trait Unchecked extends LeafArity {

  override type Notation = "_UNCHECKED_"
}

case object Unchecked extends Unchecked {
  override def runtimeValue: Int =
    throw new UnsupportedOperationException(s"<${this.productPrefix}: no runtime value>")
}
