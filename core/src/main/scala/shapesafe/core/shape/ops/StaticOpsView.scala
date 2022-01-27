package shapesafe.core.shape.ops

import shapesafe.core.axis.Axis
import shapesafe.core.shape.Shape.^
import shapesafe.core.shape.StaticShape
import shapesafe.core.shape.StaticShape.><

case class StaticOpsView[SELF <: StaticShape](shapeType: SELF) extends HasShape {

  type _ShapeType = SELF

  type Static = _ShapeType#Static
  def static: Static = shapeType.static

  protected[shape] def _and[THAT <: Axis](
      axis: THAT
  ): ><[_ShapeType, THAT] = {

    new ><(shapeType, axis)
  }

  /**
    * Same as appendInner, but can be also used on non-parametric Axis by using only its dependent type
    */
  case object & {

    def apply(
        axis: Axis
    ): ^[><[_ShapeType, axis._Axis]] = {

      _and[axis._Axis](axis).^
    }
  }

  lazy val and: &.type = &
}
