package org.shapesafe.core.shape.ops

import org.shapesafe.core.axis.Axis
import org.shapesafe.core.shape.LeafShape
import org.shapesafe.core.shape.LeafShape.><

class LeafOps[SELF <: LeafShape](val self: SELF) {

  type Static = self.Static
  def static: Static = self.static

  case object >|< {

//    def apply(
//        arity: ArityAPI
//    ): ><^[SELF, arity.Internal] = {
//
//      new LeafShape.><^(self, arity.internal)
//    }

    def apply[THAT <: Axis](
        axis: THAT
    ): ><[SELF, THAT] = {

      new LeafShape.><(self, axis)
    }
  }

  lazy val append: >|<.type = >|<
}
