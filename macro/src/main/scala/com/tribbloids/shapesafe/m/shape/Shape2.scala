//package com.tribbloids.shapesafe.m.shape
//import com.tribbloids.shapesafe.m.arity.{Dim, Expression, NameT}
//import shapeless.labelled.FieldType
//import shapeless.ops.record.Values.Aux
//import shapeless.ops.record.{Keys, Selector, Values}
//import shapeless.{::, HList, HNil, LUBConstraint}
//
//case class Shape[
//    H1 <: HList
//](
//    staticDims: H1,
//    dynamicDims: Map[NameT, Expression] = Map.empty // not used at the moment
//)(
//    implicit
//    val bound: LUBConstraint[H1, Dim.UB],
//    val keys: Keys[H1],
//    val values: Values[H1]
//) extends ShapeLike {
//
//  // implement tensor contraction/einsum???
//
//  //  def reify(
//  //      implicit
//  //
//  //           ) = {
//  //
//  //    this.copy(
//  //
//  //    )
//  //  }
//
////  def einSum[
////    H2 <: HList
////  ](that: Shape[H2])(
////    thisDim: Witness,
////    thatDim: Witness
////  )(
////     implicit
////     selectThis: Selector[H1, thisDim.T] { type Out <: Expression },
////     selectThat: Selector[H2, thatDim.T] { type Out <: Expression }
////   ) = {
////
////    val thisSelected = this.staticDims.apply(thisDim)
////    val thatSelected = that.staticDims.apply(thatDim)
////  }
//
//  //  case class EinSum[
//  //      H2 <: HList
//  //  ](that: Shape[H2])(
//  //      thisDim: Witness.Lt[String],
//  //      thatDim: Witness.Lt[String]
//  //  )(
//  //      implicit
//  //      selectThis: Selector[H1, thisDim.T] { type Out <: Operand },
//  //      selectThat: Selector[H2, thatDim.T] { type Out <: Operand }
//  //  ) {
//  //
//  //    def apply()(implicit proveEqual: selectThis.Out MayEqual selectThat.Out ~~> Proof): Unit = {}
//  //  }
//}
//
//object Shape {
//
//  type Empty = Shape[HNil]
//  object Empty extends Shape(HNil: HNil)
//
//  def |>[N <: NameT, V <: Expression](
//      dim: Dim[N, V]
//  ) = Empty cross dim
//
//  implicit class NonEmptyOps[HH <: Dim.UB, TT <: HList](self: Shape[HH :: TT]) {
//
//    def head: HH = self.staticDims.head
//
//    object tailLUB extends LUBConstraint[TT, Dim.UB]
//
//    def tail: Shape[TT] = self.copy(self.staticDims.tail)(
//      tailLUB
//    )
//  }
//
//  implicit def hlistValues[D <: Dim.UB, T <: HList](implicit vt: Values[T]): Aux[FieldType[K, V] :: T, V :: vt.Out] =
//    new Values[FieldType[K, V] :: T] {
//      type Out = V :: vt.Out
//      def apply(l: FieldType[K, V] :: T): Out = (l.head: V) :: vt(l.tail)
//    }
//}
