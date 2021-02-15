package org.shapesafe.core.shape.binary

import org.shapesafe.core.arity.binary.Op2Like
import org.shapesafe.core.shape.Shape
import shapeless.HList
import shapeless.ops.hlist.ZipWith

case class ElementWiseFactory[
    OP <: Op2Like
](
    op: OP
) {

  val poly2: op.AsShapelessPoly2.type = op.AsShapelessPoly2

  def apply[
      LL <: Shape,
      RR <: Shape,
      H_OUT <: HList
  ](
      left: LL,
      right: RR
  )(
      implicit
      zipWith: ZipWith.Aux[left.dimensions.Static, right.dimensions.Static, op.AsShapelessPoly2.type, H_OUT],
      fromIndex: Shape.FromIndex.Case[H_OUT]
  ): Shape.FromIndex.Case[H_OUT]#Out = {

    val zipped = left.dimensions.static.zipWith(right.dimensions.static) {
      poly2
    }

    Shape.FromIndex.apply(zipped)
  }
}
