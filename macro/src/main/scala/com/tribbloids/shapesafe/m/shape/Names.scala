package com.tribbloids.shapesafe.m.shape

import com.tribbloids.shapesafe.m.tuple.StaticTuples
import shapeless.Witness

import scala.language.implicitConversions

object Names extends StaticTuples.Total[String] {

  implicit class Infix[SELF <: Impl](self: SELF) {

    def ><(name: Witness.Lt[String]): SELF >< name.T = {

      new ><(self, name.value.asInstanceOf[name.T])
    }
  }

  implicit def toEyeInfix(s: Names.type): Infix[s.Eye] = Infix(s.Eye)
}
