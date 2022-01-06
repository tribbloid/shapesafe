package shapesafe.core.shape.ops

import shapesafe.core.shape.Shape

trait HasShape {

  type _Shape <: Shape
  def shape: _Shape

  lazy val api = shape.^
}

object HasShape {}
