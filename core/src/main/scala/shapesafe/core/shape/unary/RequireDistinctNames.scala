package shapesafe.core.shape.unary

import shapesafe.core.debugging.Notations
import shapesafe.core.shape.StaticShape

// all names must be distinctive - no duplication allowed
object RequireDistinctNames extends Require1 {

  object _Lemma extends RecordLemma.ConsNewName

  override type Condition[P1 <: StaticShape] = _Lemma.Case[P1#Record]

  override type _NotationProto[N] = Notations.RequireDistinctName[N]

  override type _RefuteProto = "Names has duplicates"
}
