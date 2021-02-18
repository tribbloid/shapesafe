package org.shapesafe.core.shape

import org.shapesafe.core.axis.Axis
import org.shapesafe.core.shape.ProveShape.~~>
import org.shapesafe.core.shape.ops.{LeafShapeOps, ShapeOps}
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

  final def simplify[
      SELF >: this.type <: Shape,
      O <: LeafShape
  ](
      implicit
      prove: SELF ~~> O
  ): O = prove.apply(this).out

  final def eval[ // TODO: eval each member!
      SELF >: this.type <: Shape,
      O <: LeafShape
  ](
      implicit
      prove: SELF ~~> O
  ): O = simplify(prove)
}

object Shape extends NatProductArgs {

  import LeafShape._

  implicit def toEyeOps(v: this.type): LeafShapeOps[Eye] = LeafShape.toOps[Eye](Eye)

  implicit def toOps[T <: Shape](self: T): ShapeOps[T] = new ShapeOps(self)

  // TODO: should the reverse be justified?
  def applyNatProduct[H1 <: HList, H2 <: HList](
      v: H1
  )(
      implicit
      reverse: Reverse.Aux[H1, H2],
      ev: FromNats.Case[H2]
  ): ev.Out = {
    FromNats.apply(v.reverse)
  }

  type Vector[T <: Axis] = Eye >< T

  type Matrix[T1 <: Axis, T2 <: Axis] = Eye >< T1 >< T2
}
