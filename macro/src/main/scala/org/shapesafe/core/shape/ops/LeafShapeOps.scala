package org.shapesafe.core.shape.ops

import org.shapesafe.core.axis.Axis
import org.shapesafe.core.shape.LeafShape.><
import org.shapesafe.core.shape.{LeafShape, Names}
import shapeless.HList
import shapeless.ops.hlist.Mapper

class LeafShapeOps[SELF <: LeafShape](val self: SELF) {

  type Static = self.Static
  def static: Static = self.static

  def >|<[THAT <: Axis](
      axis: THAT
  ): ><[SELF, THAT] = {

    new LeafShape.><(self, axis)
  }

  def append[THAT <: Axis](
      that: THAT
  ): ><[SELF, THAT] = {

    >|<(that)
  }

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
