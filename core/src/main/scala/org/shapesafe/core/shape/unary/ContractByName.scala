package org.shapesafe.core.shape.unary

import org.shapesafe.core.arity.ops
import org.shapesafe.core.arity.ops.ArityOps
import org.shapesafe.core.shape.Shape

import scala.language.implicitConversions

// TODO: useless, EinSum handles every thing, remove?
case class ContractByName[
    S1 <: Shape
](
    override val s1: S1 with Shape
) extends ContractByName.op._On[S1] {}

object ContractByName {

  val op: ops.ArityOps.==!._SquashByName.type = ArityOps.==!._SquashByName

  def indexing: op._Lemma.type = op._Lemma
}
