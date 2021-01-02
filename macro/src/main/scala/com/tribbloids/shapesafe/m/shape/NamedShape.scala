package com.tribbloids.shapesafe.m.shape

import com.tribbloids.shapesafe.m.arity.Expression
import shapeless.labelled.FieldType
import shapeless.ops.record.Selector
import shapeless.{::, HList, LUBConstraint, Witness}

case class NamedShape[
    H1 <: HList
](
    dimensions: H1
)(
    implicit
    val bound: LUBConstraint[H1, FieldType[_ <: String, Expression]]
) extends Shape {

  import com.tribbloids.shapesafe.m.shape.NamedShape._
  import shapeless._
  import record._

//  def |[F <: Field](element: F): NamedShape[F :: H1] = {
//
//    this.copy(element :: dimensions)
//  }

//  def |[K <: String, V <: Expression](field: FieldType[K, V]): NamedShape[FieldType[K, V] :: H1] = {
//
//    NamedShape(field :: dimensions)
//  }

  def |[
      I <: Expression
  ](dim: I)(
      implicit canName: CanName[I]
  ): NamedShape[canName.Out :: H1] = {

    val named = canName(dim)
    NamedShape(named :: dimensions)
  }

  // appender
//  case class |(name: Witness.Lt[String]) {
//
//    def |>[T <: Expression](dim: T): NamedShape[Tuple2[name.T, T] :: H1] = {
//
//      val newField: Tuple2[name.T, T] = name.value -> dim
//      NamedShape(newField :: dimensions)
////      ???
//    }
//  }

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

  trait CanName[I <: Expression] {

    type Out <: FieldType[_ <: String, Expression]

    def apply(v: I): Out
  }

  trait CanName_Imp0 {

    implicit def _nameless[I <: Expression]: Nameless[I] = new Nameless[I]

    class Nameless[I <: Expression] extends CanName[I] {

      type Out = FieldType[nameless.T, I]

      override def apply(v: I): FieldType[nameless.T, I] = v <<- nameless // v <<- nameless
    }
  }

  object CanName extends CanName_Imp0 {

    implicit def _passThrough[Base <: Expression]: PassThrough[Base] = new PassThrough[Base]

    class PassThrough[Base <: Expression] extends CanName[FieldType[_ <: String, Base]] {

      type Out = FieldType[_ <: String, Base]

      override def apply(v: FieldType[_ <: String, Base]): FieldType[_ <: String, Base] = v
    }
  }

  val nameless: Witness.Lt[String] = Witness("")

  type Bound = FieldType[_ <: String, Expression]

  implicit class NonEmptyOps[HH <: Bound, TT <: HList](self: NamedShape[HH :: TT]) {

    def head: HH = self.dimensions.head

    object tailLUB extends LUBConstraint[TT, Bound]

    def tail: NamedShape[TT] = self.copy(self.dimensions.tail)(
      tailLUB
    )
  }
}
