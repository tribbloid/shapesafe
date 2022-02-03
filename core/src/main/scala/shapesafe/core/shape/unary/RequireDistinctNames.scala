package shapesafe.core.shape.unary

import shapesafe.core.debugging.{Notations, Reporters}
import shapesafe.core.shape.{LeafShape, ProveShape, ShapeType, StaticShape}
import shapesafe.m.viz.VizCTSystem.EmitError

// all names must be distinctive - no duplication allowed
case class RequireDistinctNames[
    S1 <: ShapeType
](
    s1: S1 with ShapeType
) extends Conjecture1.^[S1] {

  override type Notation = Notations.RequireDistinctName[S1#Notation]

  override type _Refute = "Names has duplicates"
}

trait RequireDistinctName_Imp0 {

  import ProveShape._

  implicit def refute[
      S1 <: ShapeType,
      P1 <: LeafShape,
      MSG
  ](
      implicit
      lemma: S1 |- P1,
      refute0: Reporters.ForShape.Refute0[RequireDistinctNames[P1], MSG],
      msg: EmitError[MSG]
  ): RequireDistinctNames[S1] |- LeafShape = {
    ???
  }
}

object RequireDistinctNames extends RequireDistinctName_Imp0 {

  import ProveShape._

  implicit def simplify[
      S1 <: ShapeType,
      P1 <: StaticShape
  ](
      implicit
      lemma: S1 |- P1,
      indexing: _Lemma.Case[P1#Record]
  ): RequireDistinctNames[S1] |- P1 = {

    ProveShape.forAll[RequireDistinctNames[S1]].=>> { v =>
      lemma.instanceFor(v.s1)
    }
  }

  object _Lemma extends RecordLemma.ConsNewName
//  type _Lemma = _Lemma.type
}
