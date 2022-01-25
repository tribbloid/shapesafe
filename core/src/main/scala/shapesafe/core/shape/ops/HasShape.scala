package shapesafe.core.shape.ops

import shapesafe.core.shape.{Shape, ShapeType}

trait HasShape {

  type _ShapeType <: ShapeType
  def shapeType: _ShapeType

  lazy val shape: Shape.^[_ShapeType] = shapeType.^
}

object HasShape {}
