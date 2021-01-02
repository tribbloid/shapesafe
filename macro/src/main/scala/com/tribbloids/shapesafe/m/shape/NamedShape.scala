package com.tribbloids.shapesafe.m.shape

import com.tribbloids.shapesafe.m.arity.Expression
import shapeless.ops.record.Selector
import shapeless.{::, HList, LUBConstraint}

case class NamedShape[
    H1 <: HList
](
    dimensions: H1
)(
    implicit
    val bound: LUBConstraint[H1, Expression.NamedUB]
) extends Shape {

  import shapeless._
  import record._

  def cross[N <: Expression.Name, V <: Expression](
      dim: Expression.Named[N, V]
  ): NamedShape[Expression.Named[N, V] :: H1] = {

    NamedShape(dim :: dimensions)
  }

  // DON'T Refactor! `|` has the lowest operator priority
  def |[N <: Expression.Name, V <: Expression](
      dim: Expression.Named[N, V]
  ) = cross(dim)

  // implement tensor contraction/einsum???

//  def reify(
//      implicit
//
//           ) = {
//
//    this.copy(
//
//    )
//  }

  def einSum[
      H2 <: HList
  ](that: NamedShape[H2])(
      thisDim: Witness,
      thatDim: Witness
  )(
      implicit
      selectThis: Selector[H1, thisDim.T] { type Out <: Expression },
      selectThat: Selector[H2, thatDim.T] { type Out <: Expression }
  ) = {

    val thisSelected = this.dimensions.apply(thisDim)
    val thatSelected = that.dimensions.apply(thatDim)
  }

//  case class EinSum[
//      H2 <: HList
//  ](that: Shape[H2])(
//      thisDim: Witness.Lt[String],
//      thatDim: Witness.Lt[String]
//  )(
//      implicit
//      selectThis: Selector[H1, thisDim.T] { type Out <: Operand },
//      selectThat: Selector[H2, thatDim.T] { type Out <: Operand }
//  ) {
//
//    def apply()(implicit proveEqual: selectThis.Out MayEqual selectThat.Out ~~> Proof): Unit = {}
//  }
}

object NamedShape {

  implicit class NonEmptyOps[HH <: Expression.NamedUB, TT <: HList](self: NamedShape[HH :: TT]) {

    def head: HH = self.dimensions.head

    object tailLUB extends LUBConstraint[TT, Expression.NamedUB]

    def tail: NamedShape[TT] = self.copy(self.dimensions.tail)(
      tailLUB
    )
  }
}
