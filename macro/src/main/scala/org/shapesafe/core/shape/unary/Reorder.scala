package org.shapesafe.core.shape.unary

import org.shapesafe.core.Poly1Base
import org.shapesafe.core.axis.Axis
import org.shapesafe.core.shape.{ProveShape, _}

case class Reorder[ // last step of einsum, contract, transpose, etc.
    +S1 <: Shape,
    +II <: IndicesLike
](
    s1: S1,
    indices: II
) extends Shape {}

object Reorder {

  import ProveShape._

  //TODO: only 1 in superclass needs to be defined
  implicit def reduce[
      S1 <: Shape,
      P1 <: LeafShape,
      II <: IndicesLike
  ](
      implicit
      lemma1: |~~[S1, P1],
      lemma2: Premise.Case[Reorder[P1, II#Canonical]]
  ): Reorder[S1, II] =>> lemma2.Out = {

    forAll[Reorder[S1, II]].=>> { v =>
      val p1: P1 = lemma1.valueOf(v.s1)
      val vv = v.copy(s1 = p1, indices = v.indices.canonical)

      lemma2.apply(vv)
    }
  }

  object Premise extends Poly1Base[Reorder[_, _], LeafShape] {

    implicit def eye[
        P1 <: LeafShape
    ] = {

      forAll[Reorder[P1, Indices.Eye]].==> { v =>
        LeafShape.Eye
      }
    }

    implicit def inductive[
        P1 <: LeafShape,
        II_- <: Indices,
        OO_- <: LeafShape,
        I <: Index,
        O <: Axis
    ](
        implicit
        forTail: Reorder[P1, II_-] |-- OO_-,
        forHead: Get.Premise.Case.Aux[Get[P1, I], O]
    ) = {
      forAll[Reorder[P1, Indices.><[II_-, I]]].==> { v =>
        val tail: OO_- = forTail.valueOf(v.copy(indices = v.indices.tail))

        val head: O = forHead(Get(v.s1, v.indices.head))

        tail >|< head
      }
    }
  }
}
