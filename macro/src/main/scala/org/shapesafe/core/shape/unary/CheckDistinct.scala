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

  implicit def reduce[
      S1 <: Shape,
      P1 <: LeafShape
  ](
      implicit
      lemma: S1 =>> P1,
      indexing: _Indexing.Case[P1#Record]
  ): CheckDistinct[S1] =>> P1 = {

    ProveShape.forAll[CheckDistinct[S1]].=>> { v =>
      lemma.valueOf(v.s1)
    }
  }

  object _Indexing extends ReIndexing.Distinct
  type _Indexing = _Indexing.type
}
