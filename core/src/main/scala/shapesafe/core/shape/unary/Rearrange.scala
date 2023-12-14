package shapesafe.core.shape.unary

import shapesafe.core.AdHocPoly1
import shapesafe.core.axis.Axis
import shapesafe.core.debugging.Notations
import shapesafe.core.shape._

case class Rearrange[ // last step of einsum, contract, transpose, etc.
    S1 <: ShapeType,
    II <: IndicesMagnet
](
    s1: S1 with ShapeType,
    indices: II
) extends Conjecture1.On[S1] {

  override type Notation = Notations.Rearrange[S1#Notation, indices.AsIndices#Notation]

  override type _RefuteTxt = "Indices not found"
}

object Rearrange extends Rearrange_Imp0 {

  import ProveShape._

  // TODO: only 1 in superclass needs to be defined
  implicit def simplify[
      S1 <: ShapeType,
      P1 <: StaticShape,
      II <: IndicesMagnet
  ](
      implicit
      lemma1: S1 |- P1,
      lemma2: Premise.Case[Rearrange[P1, II#AsIndices]]
  ): Rearrange[S1, II] |- lemma2.Out = {

    forAll[Rearrange[S1, II]].=>> { v =>
      val p1: P1 = lemma1.instanceFor(v.s1)
      val vv = v.copy(s1 = p1, indices = v.indices.asIndices: II#AsIndices)

      lemma2.apply(vv)
    }
  }

  object Premise extends AdHocPoly1[Rearrange[_, _], StaticShape] {

    implicit def eye[
        P1 <: StaticShape
    ] = {

      forAll[Rearrange[P1, Indices.Eye]].=>> { _ =>
        StaticShape.Eye
      }
    }

    implicit def inductive[
        P1 <: StaticShape,
        II_- <: Indices,
        OO_- <: StaticShape,
        I <: Index,
        O <: Axis
    ](
        implicit
        forTail: Rearrange[P1, II_-] |- OO_-,
        forHead: Select1.Premise.Auxs.=>>[Select1[P1, I], O]
    ) = {
      forAll[Rearrange[P1, Indices.><[II_-, I]]].=>> { v =>
        val tail: OO_- = forTail.instanceFor(v.copy(indices = v.indices.tail))

        val head: O = forHead(Select1(v.s1, v.indices.head))

        tail.^ _and head
      }
    }
  }
}
