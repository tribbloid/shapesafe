package org.shapesafe.core.shape

import org.shapesafe.core.tuple.StaticTuples

import scala.language.implicitConversions

object Indices extends StaticTuples.Total[Index.Key_<:[String]] { // collection of Finder

  implicit class Infix[SELF <: Impl](self: SELF) {

    def ><[T <: Index.Str](neo: T): SELF >< T = {

      new ><(self, neo)
    }
  }

  implicit def toEyeInfix(s: Indices.type): Infix[s.Eye] = Infix(Eye)
}
