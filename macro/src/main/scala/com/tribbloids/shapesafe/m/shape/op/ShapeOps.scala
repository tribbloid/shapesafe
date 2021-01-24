package com.tribbloids.shapesafe.m.shape.op

import com.tribbloids.graph.commons.util.debug.print_@
import com.tribbloids.graph.commons.util.viz.VizType
import com.tribbloids.shapesafe.m.arity.Expression
import com.tribbloids.shapesafe.m.axis.Axis.:<<-
import com.tribbloids.shapesafe.m.axis.NameUB
import com.tribbloids.shapesafe.m.shape.Shape
import shapeless.HList
import shapeless.ops.hlist.Prepend

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
  ) = ><(axis)

  def ><><[
      THAT <: Shape,
      H_OUT <: HList
//      OUT <: Shape
  ](
      that: THAT
  )(
      implicit
      prepend: Prepend.Aux[self.Static, that.Static, H_OUT]
//      prove: Shape.FromStatic.==>[H_OUT, Shape]
  ): Unit = {

    val concat: H_OUT = self.static ++ that.static

    print_@(VizType.infer(concat).toString)

//    Shape.FromStatic(concat)
  }

  def concat[
      THAT <: Shape,
      H_OUT <: HList,
      OUT <: Shape
  ](
      that: THAT
  )(
      implicit
      prepend: Prepend.Aux[self.Static, that.Static, H_OUT],
      prove: Shape.FromStatic.==>[H_OUT, OUT]
  ) = ><><(that)
}
