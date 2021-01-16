//package com.tribbloids.shapesafe.m.shape.nullary
//
//import com.tribbloids.shapesafe.m.arity.Expression
//import com.tribbloids.shapesafe.m.axis.Axis.:<<-
//import com.tribbloids.shapesafe.m.shape.OfShape.~~>
//import com.tribbloids.shapesafe.m.shape.Shape.><
//import com.tribbloids.shapesafe.m.shape.{OfShape, Shape}
//import shapeless.labelled.FieldType
//import shapeless.{::, HList, HNil, Witness}
//
//// TODO: to be discarded after Shape becomes a TupleSystem
//class OfStatic[O <: Shape](val out: O) extends OfShape.Out_=[O] {}
//
//object OfStatic {
//
//  implicit def hNil: HNil ~~> OfStatic[Shape.Eye] = { _ =>
//    new OfStatic(Shape.Eye)
//  }
//
//  implicit def recursive[
//      TAIL <: HList,
//      PREV <: Shape,
//      N <: String,
//      V <: Expression
//  ](
//      implicit
//      proveTail: TAIL ~~> OfStatic[PREV],
//      name: Witness.Aux[N]
//  ): (FieldType[N, V] :: TAIL) ~~> OfStatic[PREV >< (V :<<- N)] = {
//
//    { v =>
//      val tail = proveTail(v.tail)
//      val value = v.head: V
//
//      val head = value :<<- name
//      val result = new Shape.><(tail.out, head)
//
//      new OfStatic(result)
//    }
//  }
//
//  def observe[H <: HList, O <: Shape](v: H)(implicit prove: H ~~> OfStatic[O]): O = {
//
//    val p = prove(v)
//    p.out
//  }
//}
