package shapesafe.core.shape

import shapesafe.core.debugging.ExpressionType

trait ShapeType extends ExpressionType {}

object ShapeType {

  implicit class converters[S <: ShapeType](self: S) {

    def ^ : Shape.^[S] = Shape.^(self)
  }
}
