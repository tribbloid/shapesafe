package org.shapesafe.core.shape

import org.shapesafe.core.tuple.StaticTuples
import shapeless.Witness

import scala.language.implicitConversions

object Names extends StaticTuples.Total[String] {

  implicit class Infix[SELF <: Impl](self: SELF) {

    def ><(name: Witness.Lt[String]): SELF >< name.T = {

      new ><(self, name.value.asInstanceOf[name.T])
    }
  }

  implicit def toEyeInfix(s: Names.type): Infix[s.Eye] = Infix(Eye)

  trait Syntax {

    implicit def literalToNames(v: String)(implicit w: Witness.Aux[v.type]): Eye >< v.type = {

      Eye >< w
    }

    implicit def literalToInfix(v: String)(implicit w: Witness.Aux[v.type]): Infix[Eye >< v.type] = {

      Infix(Eye >< w)
    }
  }

  object Syntax extends Syntax
}
