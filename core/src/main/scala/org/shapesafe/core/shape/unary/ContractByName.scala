package org.shapesafe.core.shape.unary

import org.shapesafe.core.arity.ops
import org.shapesafe.core.arity.ops.ArityOps
import org.shapesafe.core.shape.Shape

import scala.language.implicitConversions

// TODO: useless, remove?
case class ContractByName[
    S1 <: Shape
](
    override val s1: S1
) extends ContractByName.op._On[S1] {}

object ContractByName {

  val op: ops.ArityOps.:==!.SquashByName.type = ArityOps.:==!.SquashByName

  def indexing: op._Indexing.type = op._Indexing
}
