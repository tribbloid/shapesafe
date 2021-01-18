//package com.tribbloids.shapesafe.m.shape
//
//import com.tribbloids.shapesafe.m.arity.Expression
//import com.tribbloids.shapesafe.m.tuple.{CanInfix, TupleSystem}
//import shapeless.labelled.FieldType
//import shapeless.{::, HList, HNil}
//
//trait EinSumOperand[+R <: HList] { // R for record
//
//}
//
//object EinSumOperand extends TupleSystem with CanInfix {
//
//  override type UpperBound = FieldType[_ <: String, Expression]
//
//  type Impl = EinSumOperand[_ <: HList]
//
//  override object Eye extends EinSumOperand[HNil]
//
//  trait ><[
//      TAIL <: Impl,
//      HEAD <: UpperBound
//  ] extends EinSumOperand[HList]
//
//  class Cross[
//      RTAIL <: HList,
//      HEAD <: UpperBound
//  ](
//      val tail: EinSumOperand[RTAIL],
//      val head: HEAD
//  ) extends EinSumOperand[HEAD :: RTAIL]
//      with ><[EinSumOperand[RTAIL], HEAD]
//
//  implicit def canCrossIfNoConflict[
//      R,
//      N <: String,
//      D <: Expression
//  ]() = new CanCross[EinSumOperand[R], FieldType[N, D]] {
//
//    override def apply(tail: EinSumOperand[R], head: FieldType[N, D]): EinSumOperand[R] >< FieldType[N, D] = ???
//  }
//}
