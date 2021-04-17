package org.shapesafe.core.shape.unary

import org.shapesafe.core.arity.LeafArity
import org.shapesafe.core.debugging.InfoCT.{CanRefute, ErrorMsg, ForShape, Peek}
import org.shapesafe.core.shape.{LeafShape, ProveShape, Shape}

// all names must be distinctive - no duplication allowed
case class CheckDistinct[
    S1 <: Shape
](
    s1: S1 with Shape
) extends Conjecture1.^[S1] {

  override type _Peek = Peek.PrefixW1["Distinct", S1]

  override type _Refute = "Names has duplicates"
}

trait CheckDistinct_Imp0 {

  import ProveShape.Factory._
  import ProveShape._

  implicit def refute[
      S1 <: Shape,
      P1 <: LeafShape
  ](
      implicit
      lemma: S1 |- P1,
      msg: ErrorMsg[ForShape.Refute0[CheckDistinct[P1]]]
  ): CheckDistinct[S1] =>> LeafShape = {
    ???
  }
}

object CheckDistinct extends CheckDistinct_Imp0 {

  import ProveShape.Factory._
  import ProveShape._

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
