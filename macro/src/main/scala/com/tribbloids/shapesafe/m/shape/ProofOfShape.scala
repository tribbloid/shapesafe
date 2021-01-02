package com.tribbloids.shapesafe.m.shape

import com.tribbloids.shapesafe.m.arity.Expression.emptyName
import com.tribbloids.shapesafe.m.arity.{Arity, Expression, ProofOfArity}
import com.tribbloids.shapesafe.m.{~~>, Proof}
import shapeless._
import shapeless.labelled.FieldType

trait ProofOfShape extends Proof {

  def in: Shape

  override type Out <: Shape
}

object ProofOfShape {

  type Lt[O <: Shape] = ProofOfShape {
    type Out <: O
  }

  trait Named[T <: HList] extends ProofOfShape {

    final override type Out = NamedShape[T]
  }

  implicit def proveEmpty: Shape.Empty ~~> Named[HNil] = { v =>
    new Named[HNil] {

      override def in: Shape = v

      override def out: Out = v
    }
  }

//  implicit def proveByInduction[
//      N <: Expression.Name,
//      V <: Expression,
//      T <: HList,
//      VOut <: Arity,
//      TOut <: HList
//  ](
//      implicit
//      proveHead: V ~~> ProofOfArity.Lt[VOut],
//      proveTail: NamedShape[T] ~~> Named[TOut]
//  ): NamedShape[FieldType[N, V] :: T] ~~> Named[FieldType[N, VOut] :: TOut] = { v =>
//    new Named[FieldType[N, VOut] :: TOut] {
//
//      override def in: Shape = v
//
//      override lazy val out: Out = {
//
//        val headProof = proveHead(v.head)
//        val headOut: FieldType[N, VOut] = headProof.out.named[N]
//
//        val tailProof = proveTail(v.tail)
//        val tailOut: NamedShape[TOut] = tailProof.out
//
//        val result: NamedShape[FieldType[N, VOut] :: TOut] = tailOut | headOut
//
//        result
//      }
//    }
//  }
}
