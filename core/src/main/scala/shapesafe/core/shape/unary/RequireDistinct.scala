package shapesafe.core.shape.unary

import shapesafe.core.debugging.{Expressions, Reporters}
import shapesafe.core.shape.{LeafShape, ProveShape, ShapeType, StaticShape}
import shapesafe.m.viz.VizCTSystem.EmitError

// all names must be distinctive - no duplication allowed
case class RequireDistinct[
    S1 <: ShapeType
](
    s1: S1 with ShapeType
) extends Conjecture1.^[S1] {

  override type Expr = Expressions.RequireDistinct[S1#Expr]

  override type _Refute = "Names has duplicates"
}

trait RequireDistinct_Imp0 {

  import ProveShape._

  implicit def refute[
      S1 <: ShapeType,
      P1 <: LeafShape,
      MSG
  ](
      implicit
      lemma: S1 |- P1,
      refute0: Reporters.ForShape.Refute0[RequireDistinct[P1], MSG],
      msg: EmitError[MSG]
  ): RequireDistinct[S1] |- LeafShape = {
    ???
  }
}

object RequireDistinct extends RequireDistinct_Imp0 {

  import ProveShape._

  implicit def simplify[
      S1 <: ShapeType,
      P1 <: StaticShape
  ](
      implicit
      lemma: S1 |- P1,
      indexing: _Lemma.Case[P1#Record]
  ): RequireDistinct[S1] |- P1 = {

    ProveShape.forAll[RequireDistinct[S1]].=>> { v =>
      lemma.instanceFor(v.s1)
    }
  }

  object _Lemma extends RecordLemma.ConsNewName
//  type _Lemma = _Lemma.type
}
