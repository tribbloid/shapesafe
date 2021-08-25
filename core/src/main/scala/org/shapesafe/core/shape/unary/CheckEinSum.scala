package org.shapesafe.core.shape.unary

import org.shapesafe.core.arity
import org.shapesafe.core.arity.ops.ArityOps
import org.shapesafe.core.shape.Shape

// all names must be distinctive - no duplication allowed
case class CheckEinSum[
    S1 <: Shape
](
    s1: S1 with Shape
) extends CheckEinSum.op._On[S1] {}

object CheckEinSum {

  val op: arity.ops.ArityOps.==!._AppendByName.type = ArityOps.==!._AppendByName

  def indexing: op._Lemma.type = op._Lemma
}
