package shapesafe.core.shape.unary

import shapesafe.core.Ops
import shapesafe.core.shape.ShapeType

// all names must be distinctive - no duplication allowed
case class CheckEinSum[
    S1 <: ShapeType
](
    s1: S1 with ShapeType
) extends CheckEinSum.op._On[S1] {}

object CheckEinSum {

  val op: Ops.==!._ReduceByName.type = Ops.==!._ReduceByName

  def indexing: op._Lemma.type = op._Lemma
}
