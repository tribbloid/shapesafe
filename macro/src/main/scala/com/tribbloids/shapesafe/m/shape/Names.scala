package com.tribbloids.shapesafe.m.shape

import shapeless.Witness

import scala.language.implicitConversions

object Names extends TupleSystem[String] {

  class Ops[SELF <: Impl](self: SELF) {

    def ><(name: Witness.Lt[String]): SELF >< name.T =
      new ><(self, name.value.asInstanceOf[name.T])
  }

  override def getOps[SELF <: Names.Impl](self: SELF) = new Ops(self)

  implicit def toEyeOps(s: Names.type): Ops[s.Eye] = getOps(s.Eye)
}
