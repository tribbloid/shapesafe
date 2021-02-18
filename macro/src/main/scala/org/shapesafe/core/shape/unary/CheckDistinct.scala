package org.shapesafe.core.shape.unary

import org.shapesafe.core.shape.ProveShape.~~>
import org.shapesafe.core.shape.{LeafShape, Shape}
import shapeless.HList

// all names must be distinctive - no duplication allowed
case class CheckDistinct[
    S1 <: Shape
](
    s1: S1
) {}

object CheckDistinct {

  implicit def asLeaf[
      S1 <: Shape,
      P1 <: LeafShape,
      HO <: HList,
      O <: LeafShape
  ](
      implicit
      lemma: S1 ~~> P1
  ) = {}
}
