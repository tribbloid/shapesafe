package org.shapesafe.core.shape.op

import org.shapesafe.core.arity.Arity
import org.shapesafe.core.axis.Axis.:<<-
import org.shapesafe.core.axis.NameWide
import org.shapesafe.core.shape.{Names, Shape}
import shapeless.HList
import shapeless.ops.hlist.{Mapper, Prepend}

class ShapeOps[SELF <: Shape](val self: SELF) {

  type Static = self.Static
  def static: Static = self.static

  import Shape.><

  def ><[
      D <: Arity,
      N <: NameWide
  ](
      axis: D :<<- N
  ): ><[SELF, D :<<- N] = {

    new Shape.><(self, axis)
  }

  def cross[
      D <: Arity,
      N <: NameWide
  ](
      axis: D :<<- N
  ): SELF >< (D :<<- N) = ><(axis)

  def ><[
      // TODO : not sure if overloading >< is a good idea
      THAT <: Shape,
      H_OUT <: HList,
      OUT <: Shape
  ](
      that: THAT
  )(
      implicit
      toConcat: Prepend.Aux[that.Static, Static, H_OUT],
      toShape: Shape.FromStatic.=:=>[H_OUT, OUT]
  ): OUT = {

    val concat: H_OUT = that.static ++ static

    Shape.FromStatic(concat)
  }

  def concat[
      THAT <: Shape,
      H_OUT <: HList,
      OUT <: Shape
  ](
      that: THAT
  )(
      implicit
      toConcat: Prepend.Aux[that.Static, Static, H_OUT],
      toShape: Shape.FromStatic.=:=>[H_OUT, OUT]
  ): OUT = ><(that)

  def transpose[
      N <: Names.Impl,
      O <: HList
  ](
      names: N
  )(
      implicit
      mapper: Mapper.Aux[self.GetField.type, names.Static, O],
      fromIndex: Shape.FromIndex.Spec[O]
  ) = {

    val newIndex: O = names.static.map(self.GetField)(mapper)
    Shape.FromIndex.apply(newIndex)
  }
}
