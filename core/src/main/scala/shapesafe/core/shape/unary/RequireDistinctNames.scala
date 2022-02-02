package shapesafe.core.shape.unary

import shapesafe.core.debugging.Notations
import shapesafe.core.shape.{ShapeType, StaticShape}

// all names must be distinctive - no duplication allowed
case class RequireDistinctNames[S1 <: ShapeType](
    override val s1: S1 with ShapeType
) extends Conjecture1.On[S1]
    with Require1Static {

  import RequireDistinctNames._

  override type Condition[P1 <: StaticShape] = _Lemma.Case[P1#Record]

  override type Notation = Notations.RequireDistinctNames[S1#Notation]

  override type _RefuteTxt = "Names has duplicates"
}

object RequireDistinctNames {

  object _Lemma extends RecordLemma.ConsNewName
}
