package com.tribbloids.shapesafe.m.shape

import com.tribbloids.shapesafe.m.arity.{Arity, Expression, ProofOfArity}
import com.tribbloids.shapesafe.m.{~~>, Proof}
import shapeless._

trait ProofOfShape extends Proof {

  def in: Shape

  override type Out <: Shape
}

object ProofOfShape {

  trait Named[T <: HList] extends ProofOfShape {

    final override type Out = NamedShape[T]
  }

  implicit def proveEmpty: NamedShape.Empty ~~> Named[HNil] = { v =>
    new Named[HNil] {

      override def in: Shape = v

      override def out: Out = v
    }
  }

  implicit def induction[
      Head <: Expression,
      Tail <: HList,
      PHead <: ProofOfArity,
      OTail <: HList
  ](
      implicit
      proveHead: Head ~~> PHead,
      proveTail: NamedShape[Tail] ~~> Named[OTail]
//      lubTail: LUBConstraint[Tail, Expression]
  ): NamedShape[Head :: Tail] ~~> Named[Head :: Tail] = { v =>
    new Named[PHead#Out :: OTail] {

      override def in: Shape = v

      val headProof: ProofOfArity = proveHead(v.head)

      val tailProof: Named[OTail] = proveTail(v.tail)

      object _out {
        val head = headProof.out
        val tail = tailProof.out

        val all: NamedShape[headProof.Out :: Tail] = head +: tail
      }

      override def out: Out = _out.all
    }
  }
}
