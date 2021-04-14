package org.shapesafe.core.shape.unary

import org.shapesafe.core.debugging.InfoCT.{Infix, Peek}
import org.shapesafe.core.shape.{LeafShape, ProveShape, Shape, ShapeConjecture}
import singleton.ops.+

// all names must be distinctive - no duplication allowed
case class CheckDistinct[
    S1 <: Shape
](
    s1: S1 with Shape
) extends ShapeConjecture {

  override type _Peek = "Distinct(" + Peek[S1] + ")"
}

object CheckDistinct {

  import ProveShape._
  import ProveShape.Factory._

  implicit def simplify[
      S1 <: Shape,
      P1 <: LeafShape
  ](
      implicit
      lemma: S1 |- P1,
      indexing: _Indexing.Case[P1#Record]
  ): CheckDistinct[S1] =>> P1 = {

    ProveShape.forAll[CheckDistinct[S1]].=>> { v =>
      lemma.valueOf(v.s1)
    }
  }

  object _Indexing extends UnaryIndexingFn.Distinct
  type _Indexing = _Indexing.type
}
