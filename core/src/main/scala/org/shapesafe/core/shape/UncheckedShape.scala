package org.shapesafe.core.shape

trait UncheckedShape extends LeafShape {}

object UncheckedShape extends UncheckedShape {

  override type _AsOpStr = this.type
  override type _AsExpr = this.type

  override def nodeString: String = ???
}
