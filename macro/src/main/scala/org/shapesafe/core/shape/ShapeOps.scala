package org.shapesafe.core.shape

import org.shapesafe.core.arity.Arity
import org.shapesafe.core.axis.Axis.:<<-
import org.shapesafe.core.axis.NameWide
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
      toShape: Shape.FromStatic.==>[H_OUT, OUT]
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
      toShape: Shape.FromStatic.==>[H_OUT, OUT]
  ): OUT = ><(that)

  def transpose[
      N <: Names.Impl,
      O <: HList
  ](
      names: N
  )(
      implicit
      mapper: Mapper.Aux[self.getField.type, names.Static, O],
      fromIndex: Shape.FromIndex.Case[O]
  ): Shape.FromIndex.Case[O]#Out = {

    val newIndex: O = names.static.map(self.getField)(mapper)
    Shape.FromIndex.apply(newIndex)
  }

//  object ElementWise {
//
//    def +[
//        THAT <: Shape,
//        H_OUT <: HList
//    ](that: THAT)(
//        implicit
//        zipWith: ZipWith.Aux[self.dimensions.Static, that.dimensions.Static, op.AsShapelessPoly2.type, H_OUT],
//        fromIndex: Shape.FromIndex.Case[H_OUT]
//    ) = {
//
//      val op2 = Op2[ops.+]()
//      val factory = ElementWiseFactory(op2)
//
//      factory.apply(self, that)
//    }
//  }

//  object Sum extends Poly2 {
//
//    implicit def plus[
//        D1 <: Arity,
//        D2 <: Arity,
//        O <: Arity
//    ]: Case[D1, D2] = new Case.Aux[D1, D2, O] {}
//  }
//
//  def elementWiseSum[
//      THAT <: Shape
//  ](
//      that: THAT
//  )(
//      implicit
//      zipWith: ZipWith[self.dimensions.Static :: that.dimensions.Static :: HNil]
//  ) = {
//    val thisD = self.dimensions.static
//    val thatD = that.dimensions.static
//
//    val zipped = thisD.zipWith(thatD) {}
//
//  }
}
