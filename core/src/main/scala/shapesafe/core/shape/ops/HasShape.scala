package shapesafe.core.shape.ops

import shapesafe.core.shape.ShapeType

trait HasShape {

  type _ShapeType <: ShapeType
  def shapeType: _ShapeType

  lazy val shape = shapeType.^
}

object HasShape {}
