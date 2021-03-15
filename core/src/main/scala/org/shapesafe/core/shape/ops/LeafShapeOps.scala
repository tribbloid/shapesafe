package org.shapesafe.core.shape.ops

import org.shapesafe.core.axis.AxisMagnet
import org.shapesafe.core.shape.LeafShape
import org.shapesafe.core.shape.LeafShape.><

class LeafShapeOps[SELF <: LeafShape](val self: SELF) {

  type Static = self.Static
  def static: Static = self.static

  case object >|< {

    def apply[THAT <: AxisMagnet](
        axisLike: THAT
    )(
        implicit
        toAxis: AxisMagnet.Case[THAT]
    ): ><[SELF, toAxis.Out] = {

      new LeafShape.><(self, toAxis(axisLike))
    }
  }

  lazy val append: >|<.type = >|<
}
