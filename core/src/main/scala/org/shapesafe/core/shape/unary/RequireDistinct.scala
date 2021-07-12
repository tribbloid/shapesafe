package org.shapesafe.core.shape.unary

import org.shapesafe.core.debugging.Expressions.Expr
import org.shapesafe.core.debugging.{Expressions, OpStrs, Reporters}
import org.shapesafe.core.shape.{ProveShape, Shape, StaticShape}
import org.shapesafe.m.viz.VizCTSystem.EmitError

// all names must be distinctive - no duplication allowed
case class RequireDistinct[
    S1 <: Shape
](
    s1: S1 with Shape
) extends Conjecture1.^[S1] {

  override type _AsOpStr = OpStrs.PrefixW1["Distinct", S1]
  override type _AsExpr = Expressions.CheckDistinct[Expr[S1]]

  override type _Refute = "Names has duplicates"
}

trait CheckDistinct_Imp0 {

  import ProveShape.Factory._
  import ProveShape._

  implicit def refute[
      S1 <: Shape,
      P1 <: StaticShape,
      MSG
  ](
      implicit
      lemma: S1 |- P1,
      refute0: Reporters.ForShape.Refute0[RequireDistinct[P1], MSG],
      msg: EmitError[MSG]
  ): RequireDistinct[S1] =>> StaticShape = {
    null
  }
}

object RequireDistinct extends CheckDistinct_Imp0 {

  import ProveShape.Factory._
  import ProveShape._

  implicit def simplify[
      S1 <: Shape,
      P1 <: StaticShape
  ](
      implicit
      lemma: S1 |- P1,
      indexing: _Indexing.Case[P1#Record]
  ): RequireDistinct[S1] =>> P1 = {

    ProveShape.forAll[RequireDistinct[S1]].=>> { v =>
      lemma.valueOf(v.s1)
    }
  }

  object _Indexing extends UnaryIndexingFn.Distinct
  type _Indexing = _Indexing.type
}
