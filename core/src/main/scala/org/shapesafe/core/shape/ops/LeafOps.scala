package org.shapesafe.core.shape.ops

import org.shapesafe.core.axis.Axis
import org.shapesafe.core.shape.LeafShape
import org.shapesafe.core.shape.LeafShape.><
import org.shapesafe.core.shape.ShapeAPI.^

case class LeafOps[SELF <: LeafShape](shape: SELF) extends HasShape {

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
  case object >|< {

    // TODO: this convoluted, recursive type bound further shows the necessity for scala to have built-in non-singleton peer type
    def apply[THAT <: Axis { type _Axis <: THAT }](
        axis: THAT
    ): ^[><[_Shape, axis._Axis]] = {

      appendInner(axis.axis).^
    }
  }

  lazy val append: >|<.type = >|<
}
