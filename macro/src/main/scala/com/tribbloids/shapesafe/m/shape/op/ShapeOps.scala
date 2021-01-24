package com.tribbloids.shapesafe.m.shape.op

import com.tribbloids.shapesafe.m.arity.Expression
import com.tribbloids.shapesafe.m.axis.Axis.:<<-
import com.tribbloids.shapesafe.m.axis.{Axis, NameUB}
import com.tribbloids.shapesafe.m.shape.Shape

class ShapeOps[SELF <: Shape](self: SELF) {

  import Shape.><

  def ><[
      D <: Expression,
      N <: NameUB
  ](
      axis: Axis.Named[D, N]
  ): SELF >< (D :<<- N) = {

    new Shape.><(self, axis.asField)
  }

  def cross[
      D <: Expression,
      N <: NameUB
  ](
      axis: Axis.Named[D, N]
  ): SELF >< (D :<<- N) = {

    ><(axis)
  }
}
