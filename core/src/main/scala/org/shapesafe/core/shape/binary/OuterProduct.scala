package org.shapesafe.core.shape.binary

import org.shapesafe.core.axis.Axis
import org.shapesafe.core.debugging.Expressions.Expr
import org.shapesafe.core.debugging.{Expressions, OpStrs}
import org.shapesafe.core.shape.StaticShape.><
import org.shapesafe.core.shape.ProveShape._
import org.shapesafe.core.shape.{Shape, StaticShape}
import shapeless.HList
import shapeless.ops.hlist.Prepend

case class OuterProduct[
    S1 <: Shape,
    S2 <: Shape
](
    s1: S1,
    s2: S2
) extends Conjecture2.^[S1, S2] {

  override type _AsOpStr = OpStrs.Infix[S1, " OuterProduct ", S2]
  override type _AsExpr = Expressions.><[Expr[S1], Expr[S2]]
}

trait OuterProduct_Imp0 {

  import org.shapesafe.core.shape.ProveShape.ForAll._

  //TODO: should leverage append, if the deadlock problem has been solved
  implicit def simplify[
      S1 <: Shape,
      P1 <: StaticShape,
      S2 <: Shape,
      P2 <: StaticShape,
      HO <: HList
  ](
      implicit
      lemma1: S1 |- P1,
      lemma2: S2 |- P2,
      concat: Prepend.Aux[P2#Static, P1#Static, HO],
      toShape: StaticShape.FromStatic.Case[HO]
  ): OuterProduct[S1, S2] =>> toShape.Out = {

    forAll[OuterProduct[S1, S2]].=>> { direct =>
      val p1: P1 = lemma1.valueOf(direct.s1)
      val p2: P2 = lemma2.valueOf(direct.s2)

      toShape(concat(p2.static, p1.static))
    }
  }
}

object OuterProduct extends OuterProduct_Imp0 {

  import org.shapesafe.core.shape.ProveShape.ForAll._

  // shortcut for trivial D + 1 case
  implicit def append[
      S1 <: Shape,
      P1 <: StaticShape,
      S2 <: Shape,
      A2 <: Axis,
      P2 <: StaticShape.Eye >< A2
  ](
      implicit
      lemma1: S1 |- P1,
      lemma2: S2 |- P2
  ): OuterProduct[S1, S2] =>> (P1 >< A2) = {

    forAll[OuterProduct[S1, S2]].=>> { direct =>
      val p1: P1 = lemma1.valueOf(direct.s1)
      val p2: P2 = lemma2.valueOf(direct.s2)
      val a2: A2 = p2.head

      val result = p1.^ appendInner a2

      result
    }
  }
}
