package com.tribbloids.shapesafe.m.shape.op

import com.tribbloids.shapesafe.m.arity.Expression
import com.tribbloids.shapesafe.m.axis.Axis.:<<-
import com.tribbloids.shapesafe.m.axis.NameUB
import com.tribbloids.shapesafe.m.shape.Shape

class ShapeOps[SELF <: Shape](self: SELF) {

  def ><[
      V <: Expression,
      N <: NameUB
  ](
      dim: V :<<- N
  ): Shape.><[SELF, V :<<- N] = new Shape.><(self, dim)

  def cross[
      V <: Expression,
      N <: NameUB
  ](
      dim: V :<<- N
  ) = {

    ><(dim)
  }
}
