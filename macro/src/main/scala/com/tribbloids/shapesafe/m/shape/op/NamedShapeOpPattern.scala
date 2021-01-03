//package com.tribbloids.shapesafe.m.shape.op
//
//import com.tribbloids.shapesafe.m.arity.Expression
//import com.tribbloids.shapesafe.m.shape.{Shape, Shape}
//import com.tribbloids.shapesafe.m.~~>
//import shapeless.HList
//
//trait NamedShapeOpPattern {
//
//  import shapeless.::
//
//  trait FieldOp[-T <: Expression.NamedUB, +R <: Expression.NamedUB] extends (T ~~> R) {}
//
//  trait Op[-T <: Shape, +R <: Shape] extends ShapeOp[T, R]
//
//  object Op {
//
//    implicit def mapEmpty: Op[Shape.Empty, Shape.Empty] = identity
//
//    implicit def recursively[
//        V <: Expression.NamedUB,
//        VOut <: Expression.NamedUB,
//        T <: HList,
//        TOut <: HList
//    ](
//        implicit
//        proveHead: V FieldOp VOut,
//        proveTail: Shape[T] Op Shape[TOut]
//    ): Shape[V :: T] Op Shape[VOut :: TOut] = { v =>
//      val headProof = proveHead(v.head)
//      val tailProof = proveTail(v.tail)
//
//      val result: Shape[VOut :: TOut] = tailProof | headProof
//
//      result
//    }
//  }
//
//}
