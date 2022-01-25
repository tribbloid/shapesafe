package shapesafe.core.shape.unary

import shapesafe.core.Ops
import shapesafe.core.shape.Shape

// TODO: useless, EinSum handles every thing, remove?
case class ContractByName[
    S1 <: Shape
](
    override val s1: S1 with Shape
) extends ContractByName.op._On[S1] {}

object ContractByName {

  val op: Ops.==!._ReduceByName.type = Ops.==!._ReduceByName

  def indexing: op._Lemma.type = op._Lemma
}
