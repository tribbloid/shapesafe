package org.shapesafe.core.shape.ops

import org.shapesafe.core.axis.Axis
import org.shapesafe.core.shape.LeafShape
import org.shapesafe.core.shape.LeafShape.><

class LeafOps[SELF <: LeafShape](val self: SELF) {

  type Static = self.Static
  def static: Static = self.static

  def appendInner[THAT <: Axis](
      axis: THAT
  ): ><[SELF, THAT] = {

    new LeafShape.><(self, axis)
  }

  /**
    * Same as appendInner, but can be also used on non-parametric Axis by using only its dependent type
    */
  case object >|< {

    // TODO: this convoluted, recursive type bound further shows the necessity for scala to have built-in non-singleton peer type
    def apply[THAT <: Axis { type AxisSelf <: THAT }](
        axis: THAT
    ): ><[SELF, axis.AxisSelf] = {

      appendInner(axis.axisSelf)
    }
  }

  lazy val append: >|<.type = >|<
}
