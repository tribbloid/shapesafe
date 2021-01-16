package com.tribbloids.shapesafe.m.shape.op

import com.tribbloids.shapesafe.m.arity.Expression
import com.tribbloids.shapesafe.m.axis.Axis.:<<-
import com.tribbloids.shapesafe.m.axis.NameUB
import com.tribbloids.shapesafe.m.shape.Shape

class ShapeOps[SELF <: Shape](self: SELF) {

  def ><[
      D <: Expression,
      N <: NameUB
  ](
      axis: D :<<- N
  ): Shape.><[SELF, D :<<- N] = {

    new Shape.><(self, axis)
  }

  def cross[
      D <: Expression,
      N <: NameUB
  ](
      axis: D :<<- N
  ) = {

    ><(axis)
  }
}
