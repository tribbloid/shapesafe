package org.shapesafe.core.shape.unary

import org.shapesafe.core.Poly1Base
import org.shapesafe.core.axis.Axis
import org.shapesafe.core.debugging.Expressions.Expr
import org.shapesafe.core.debugging.{DebugUtil, Expressions, OpStrs, Reporters}
import org.shapesafe.core.shape.{ProveShape, _}
import org.shapesafe.m.viz.VizCTSystem.EmitError

case class Reorder[ // last step of einsum, contract, transpose, etc.
    S1 <: Shape,
    II <: IndicesMagnet
](
    s1: S1 with Shape,
    indices: II
) extends Conjecture1.^[S1] {

  override type _AsOpStr = OpStrs.Infix[S1, " Reorder ", indices.AsIndices]
  override type _AsExpr = Expressions.Reorder[Expr[S1], Expr[indices.AsIndices]]

  override type _Refute = "Indices not found"
}

trait Reorder_Imp0 {

  import ProveShape._
  import Factory._

  implicit def refute[
      S1 <: Shape,
      P1 <: LeafShape,
      II <: IndicesMagnet,
      MSG
  ](
      implicit
      lemma: S1 |- P1,
      refute0: Reporters.ForShape.Refute0[Reorder[P1, II], MSG],
      msg: EmitError[MSG]
  ): Reorder[S1, II] =>> LeafShape = {
    ???
  }

}

object Reorder extends Reorder_Imp0 {

  import ProveShape._
  import Factory._

  //TODO: only 1 in superclass needs to be defined
  implicit def simplify[
      S1 <: Shape,
      P1 <: StaticShape,
      II <: IndicesMagnet
  ](
      implicit
      lemma1: S1 |- P1,
      lemma2: Premise.Case[Reorder[P1, II#AsIndices]]
  ): Reorder[S1, II] =>> lemma2.Out = {

    forAll[Reorder[S1, II]].=>> { v =>
      val p1: P1 = lemma1.valueOf(v.s1)
      val vv = v.copy(s1 = p1, indices = v.indices.asIndices: II#AsIndices)

      lemma2.apply(vv)
    }
  }

  object Premise extends Poly1Base[Reorder[_, _], StaticShape] {

    implicit def eye[
        P1 <: StaticShape
    ] = {

      forAll[Reorder[P1, Indices.Eye]].==> { v =>
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
        forTail: Reorder[P1, II_-] |- OO_-,
        forHead: GetSubscript.Premise.Case.Aux[GetSubscript[P1, I], O]
    ) = {
      forAll[Reorder[P1, Indices.><[II_-, I]]].==> { v =>
        val tail: OO_- = forTail.valueOf(v.copy(indices = v.indices.tail))

        val head: O = forHead(GetSubscript(v.s1, v.indices.head))

        tail.^ appendInner head
      }
    }
  }
}
