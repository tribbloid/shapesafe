package org.shapesafe.core.shape

import org.shapesafe.core.arity.Arity
import org.shapesafe.core.axis.Axis.:<<-
import shapeless.HList
import shapeless.ops.hlist.{Mapper, Prepend}

class LeafShapeOps[SELF <: LeafShape](val self: SELF) {

  type Static = self.Static
  def static: Static = self.static

  import LeafShape.><

  def ><[
      D <: Arity,
      N <: String
  ](
      axis: D :<<- N
  ): ><[SELF, D :<<- N] = {

    new LeafShape.><(self, axis)
  }

  def cross[
      D <: Arity,
      N <: String
  ](
      axis: D :<<- N
  ): SELF >< (D :<<- N) = ><(axis)

  def ><[
      // TODO : not sure if overloading >< is a good idea
      THAT <: LeafShape,
      H_OUT <: HList,
      OUT <: LeafShape
  ](
      that: THAT
  )(
      implicit
      toConcat: Prepend.Aux[that.Static, Static, H_OUT],
      toShape: LeafShape.FromStatic.==>[H_OUT, OUT]
  ): OUT = {

    val concat: H_OUT = that.static ++ static

    LeafShape.FromStatic(concat)
  }

  def concat[
      THAT <: LeafShape,
      H_OUT <: HList,
      OUT <: LeafShape
  ](
      that: THAT
  )(
      implicit
      toConcat: Prepend.Aux[that.Static, Static, H_OUT],
      toShape: LeafShape.FromStatic.==>[H_OUT, OUT]
  ): OUT = ><(that)

  def transpose[
      N <: Names,
      O <: HList
  ](
      names: N
  )(
      implicit
      mapper: Mapper.Aux[self.getField.type, names.Keys, O],
      fromIndex: LeafShape.FromRecord.Case[O]
  ): LeafShape.FromRecord.Case[O]#Out = {

    val newIndex: O = names.keys.map(self.getField)(mapper)
    LeafShape.FromRecord.apply(newIndex)
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
