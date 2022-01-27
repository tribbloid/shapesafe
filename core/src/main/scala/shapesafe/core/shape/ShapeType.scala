package shapesafe.core.shape

import shapesafe.core.debugging.CanPeek
import shapesafe.core.shape.args.{ApplyLiterals, ApplyNats}

trait ShapeType extends CanPeek {}

object ShapeType {

  implicit class converters[S <: ShapeType](self: S) {

    def ^ : Shape.^[S] = Shape.^(self)
  }
}
