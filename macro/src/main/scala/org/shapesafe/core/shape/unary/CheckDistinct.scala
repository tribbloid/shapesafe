package org.shapesafe.core.shape.unary

import org.shapesafe.core.shape.ProveShape._
import org.shapesafe.core.shape.{LeafShape, ProveShape, Shape}

// all names must be distinctive - no duplication allowed
case class CheckDistinct[
    S1 <: Shape
](
    s1: S1
) extends Shape {}

object CheckDistinct {

  implicit def asLeaf[
      S1 <: Shape,
      P1 <: LeafShape
  ](
      implicit
      lemma: S1 =>> P1,
      // TODO: if the compiler works properly this could be S1 --> P1
      //  too bad at this moment the VerifiedShape.endo cannot be successfully summoned
      indexing: DistinctIndexed.FromStatic.Case[P1#Record]
  ): CheckDistinct[S1] =>> P1 = {

    ProveShape.forAll[CheckDistinct[S1]].=>> { v =>
      lemma.valueOf(v.s1)
    }
  }
}
