package com.tribbloids.shapesafe.m.shape

import com.tribbloids.shapesafe.m.tuple.{CanCrossAlways, TupleSystem}
import shapeless.Witness

import scala.language.implicitConversions

object Names extends TupleSystem[String] with CanCrossAlways[String] {

  implicit class Ops[SELF <: Impl](self: SELF) {

    def ><(name: Witness.Lt[String]): SELF >< name.T =
      new ><(self, name.value.asInstanceOf[name.T])
  }

  implicit def toEyeOps(s: Names.type): Ops[s.Eye] = Ops(s.Eye)
}
