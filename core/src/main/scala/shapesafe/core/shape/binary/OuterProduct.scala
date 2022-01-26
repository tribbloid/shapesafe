package shapesafe.core.shape.binary

import shapesafe.core.axis.Axis
import shapesafe.core.debugging.{Expressions, Reporters}
import shapesafe.core.shape.StaticShape.><
import shapesafe.core.shape.{LeafShape, Shape, StaticShape}
import shapesafe.m.viz.VizCTSystem.EmitError
import shapeless.HList
import shapeless.ops.hlist.Prepend

case class OuterProduct[
    S1 <: Shape,
    S2 <: Shape
](
    s1: S1,
    s2: S2
) extends Conjecture2.^[S1, S2] {

  override type Expr = Expressions.><[S1#Expr, S2#Expr]

  override type _Refute = "Cannot compute outer product"
}

trait OuterProduct_Imp1 {

  import shapesafe.core.shape.ProveShape._

  implicit def refute[
      S1 <: Shape,
      P1 <: LeafShape,
      S2 <: Shape,
      P2 <: LeafShape,
      MSG
  ](
      implicit
      lemma1: S1 |- P1,
      lemma2: S2 |- P2,
      refute0: Reporters.ForShape.Refute0[OuterProduct[P1, P2], MSG],
      msg: EmitError[MSG]
  ): OuterProduct[S1, S2] |- LeafShape = {
    ???
  }
}

trait OuterProduct_Imp0 extends OuterProduct_Imp1 {

  import shapesafe.core.shape.ProveShape._

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
  ): OuterProduct[S1, S2] |- toShape.Out = {

    forAll[OuterProduct[S1, S2]].=>> { direct =>
      val p1: P1 = lemma1.instanceFor(direct.s1)
      val p2: P2 = lemma2.instanceFor(direct.s2)

      toShape(concat(p2.static, p1.static))
    }
  }
}

object OuterProduct extends OuterProduct_Imp0 {

  import shapesafe.core.shape.ProveShape._

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
  ): OuterProduct[S1, S2] |- (P1 >< A2) = {

    forAll[OuterProduct[S1, S2]].=>> { direct =>
      val p1: P1 = lemma1.instanceFor(direct.s1)
      val p2: P2 = lemma2.instanceFor(direct.s2)
      val a2: A2 = p2.head

      val result = p1.^ _and a2

      result
    }
  }
}
