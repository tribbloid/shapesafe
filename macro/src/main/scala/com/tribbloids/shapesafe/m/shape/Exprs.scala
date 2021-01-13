package com.tribbloids.shapesafe.m.shape

import com.tribbloids.shapesafe.m.arity.Expression

object Exprs extends TupleSystem[Expression] {

  class Ops[SELF <: Impl](val self: SELF) extends InfixOpsMixin[SELF] {}

  override def getOps[SELF <: Exprs.Impl](self: SELF) = new Ops(self)
}
