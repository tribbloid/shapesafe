package org.shapesafe.core.shape.unary

import org.shapesafe.core.shape.ProveShape._
import org.shapesafe.core.shape.binary.Direct
import org.shapesafe.core.shape.{LeafShape, Names, ProveShape, Shape}

import scala.language.implicitConversions

// all names must be distinctive - no duplication allowed
case class CheckEinSum[
    S1 <: Shape
](
    s1: S1
) extends Shape {

  def ->[N <: Names](names: N): Reorder[CheckEinSum[S1], N] = {

    Reorder(this, names)
  }

  def apply[S2 <: Shape](that: CheckEinSum[S2]): CheckEinSum[Direct[CheckEinSum[S1], CheckEinSum[S2]]] = {
    val direct: Direct[
      CheckEinSum[S1],
      CheckEinSum[S2]
    ] = this >< that

    CheckEinSum(direct)
  }

  def einSum: this.type = this
}

object CheckEinSum {

  implicit def asLeaf[
      S1 <: Shape,
      P1 <: LeafShape
  ](
      implicit
      lemma: S1 =>> P1,
      // TODO: if the compiler works properly this could be S1 --> P1
      //  too bad at this moment the VerifiedShape.endo cannot be successfully summoned
      indexing: EinSumIndexed.FromStatic.Case[P1#Record]
  ): CheckEinSum[S1] =>> P1 = {

    ProveShape.forAll[CheckEinSum[S1]].=>> { v =>
      lemma.valueOf(v.s1)
    }
  }

  implicit def fromShape[S1 <: Shape](v: S1): CheckEinSum[S1] = CheckEinSum(v)
}
