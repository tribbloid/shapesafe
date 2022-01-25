package shapesafe.core.shape.unary

import shapesafe.core.Ops
import shapesafe.core.shape.Shape

// all names must be distinctive - no duplication allowed
case class CheckEinSum[
    S1 <: Shape
](
    s1: S1 with Shape
) extends CheckEinSum.op._On[S1] {}

object CheckEinSum {

  val op: Ops.==!._AppendByName.type = Ops.==!._AppendByName

  def indexing: op._Lemma.type = op._Lemma
}
