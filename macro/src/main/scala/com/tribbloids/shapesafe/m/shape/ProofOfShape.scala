package com.tribbloids.shapesafe.m.shape

import com.tribbloids.shapesafe.m.arity.{Arity, Expression, ProofOfArity}
import com.tribbloids.shapesafe.m.shape.NamedShape.Bound
import com.tribbloids.shapesafe.m.{~~>, Proof}
import shapeless._

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
//      H <: Element,
//      T <: HList,
//      HO <: Arity,
//      TO <: HList
//  ](
//      implicit
//      proveHead: H ~~> ProofOfArity.Lt[HO],
//      proveTail: NamedShape[T] ~~> Named[TO]
//      //      lubTail: LUBConstraint[Tail, Expression]
//  ): NamedShape[H :: T] ~~> Named[HO :: TO] = { v =>
//    new Named[HO :: TO] {
//
//      override def in: Shape = v
//
//      override lazy val out: Out = {
//
//        val headProof = proveHead(v.head)
//        val headOut: HO = headProof.out
//
//        val tailProof = proveTail(v.tail)
//        val tailOut = tailProof.out
//
//        val result = tailOut | headOut
//
//        result
//      }
//    }
//  }
}
