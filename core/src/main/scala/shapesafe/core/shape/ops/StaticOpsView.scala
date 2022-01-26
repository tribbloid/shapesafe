package shapesafe.core.shape.ops

import shapesafe.core.axis.Axis
import shapesafe.core.shape.ShapeAPI.^
import shapesafe.core.shape.StaticShape
import shapesafe.core.shape.StaticShape.><

case class StaticOpsView[SELF <: StaticShape](shape: SELF) extends HasShape {

  type _Shape = SELF

  type Static = _Shape#Static
  def static: Static = shape.static

  protected[shape] def _and[THAT <: Axis](
      axis: THAT
  ): ><[_Shape, THAT] = {

    new ><(shape, axis)
  }

  /**
    * Same as appendInner, but can be also used on non-parametric Axis by using only its dependent type
    */
  case object & {

    def apply(
        axis: Axis
    ): ^[><[_Shape, axis._Axis]] = {

      _and[axis._Axis](axis).^
    }
  }

  lazy val and: &.type = &
}
