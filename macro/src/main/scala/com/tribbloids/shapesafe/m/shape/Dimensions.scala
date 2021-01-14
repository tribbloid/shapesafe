package com.tribbloids.shapesafe.m.shape

import com.tribbloids.shapesafe.m.TupleSystem
import com.tribbloids.shapesafe.m.arity.Expression

import scala.language.implicitConversions

object Dimensions extends TupleSystem[Expression] {

  class Ops[SELF <: Impl](val self: SELF) extends InfixOpsMixin[SELF] {}

  override def getOps[SELF <: Dimensions.Impl](self: SELF) = new Ops(self)

  implicit def toEyeOps(s: Dimensions.type): Ops[s.Eye] = getOps(s.Eye)
}
