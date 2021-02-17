package org.shapesafe.core.shape

import org.shapesafe.core.shape.LeafShape.{ops, Eye}
import org.shapesafe.core.shape.ProveShape.~~>
import shapeless.ops.hlist.Reverse
import shapeless.{HList, NatProductArgs}

import scala.language.implicitConversions

trait Shape {

  final def verify[
      SELF >: this.type <: Shape,
      O <: Shape
  ](
      implicit
      prove: SELF ~~> O
  ): O = prove.apply(this).out

  final def eval[
      SELF >: this.type <: Shape,
      O <: LeafShape
  ](
      implicit
      prove: SELF ~~> O
  ): O = prove.apply(this).out

}

object Shape extends NatProductArgs {

  implicit def toEyeOps(v: this.type): LeafShapeOps[Eye] = ops[Eye](Eye)

  implicit def toOps[T <: Shape](v: T): ShapeOps[T] = ShapeOps(v)

  // TODO: should the reverse be justified?
  def applyNatProduct[H1 <: HList, H2 <: HList](
      v: H1
  )(
      implicit
      reverse: Reverse.Aux[H1, H2],
      ev: LeafShape.FromNats.Case[H2]
  ): ev.Out = {
    LeafShape.FromNats.apply(v.reverse)
  }

}
