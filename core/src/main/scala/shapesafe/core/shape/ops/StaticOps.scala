package shapesafe.core.shape.ops

import shapesafe.core.axis.Axis
import shapesafe.core.shape.ShapeAPI.^
import shapesafe.core.shape.StaticShape
import shapesafe.core.shape.StaticShape.><

case class StaticOps[SELF <: StaticShape](shape: SELF) extends HasShape {

  type _Shape = SELF

  type Static = _Shape#Static
  def static: Static = shape.static

  def appendInner[THAT <: Axis](
      axis: THAT
  ): ><[_Shape, THAT] = {

    new ><(shape, axis)
  }

  /**
    * Same as appendInner, but can be also used on non-parametric Axis by using only its dependent type
    */
  case object & {

    // TODO: this convoluted, recursive type bound further shows the necessity for scala to have built-in non-singleton peer type
    //  axis can be a magnet type
    def apply[THAT <: Axis { type _Axis <: THAT }](
        axis: THAT
    ): ^[><[_Shape, axis._Axis]] = {

      appendInner(axis.axis).^
    }
  }

  lazy val append: &.type = &
}
