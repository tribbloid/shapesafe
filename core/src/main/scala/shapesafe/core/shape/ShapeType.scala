package shapesafe.core.shape

import shapesafe.core.debugging.CanPeek

trait ShapeType extends CanPeek {}

object ShapeType {

  implicit class converters[S <: ShapeType](self: S) {

    def ^ : Shape.^[S] = Shape.^(self)
  }
}
