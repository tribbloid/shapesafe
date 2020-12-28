package com.tribbloids.shapesafe.m.shape

import com.tribbloids.graph.commons.util.debug.print_@
import com.tribbloids.graph.commons.util.viz.VizType
import com.tribbloids.shapesafe.m.arity.Expression
import shapeless.labelled.FieldType
import shapeless.ops.record.Selector
import shapeless.{HList, LUBConstraint}

case class NamedShape[H1 <: HList](
    self: H1
)(
    implicit
    isIndexToExpr: LUBConstraint[H1, Expression]
) extends Shape {

  import shapeless._
  import record._

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

    val thisSelected = this.self.apply(thisDim)
    val thatSelected = (that.self).apply(thatDim)

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

  def main(args: Array[String]): Unit = {

    import shapeless._
    import record._
    import syntax.singleton._

    val book =
      ("author" ->> "Benjamin Pierce") ::
        ("title" ->> "Types and Programming Languages") ::
        ("id" ->> 262162091) ::
        ("price" ->> 44.11) ::
        HNil

    val rr = book.apply("author") // Note result type ...

    print_@(rr)

    print_@(VizType.infer(rr).toString)
  }
}
