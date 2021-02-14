package org.shapesafe.core.arity

import org.shapesafe.core.arity.binary.{AssertEqual, Op2}
import singleton.ops

trait Syntax {

  type `+`[X <: Arity, Y <: Arity] = Op2[ops.+]#On[X, Y]

  type `-`[X <: Arity, Y <: Arity] = Op2[ops.-]#On[X, Y]

  type `*`[X <: Arity, Y <: Arity] = Op2[ops.*]#On[X, Y]

  type `/`[X <: Arity, Y <: Arity] = Op2[ops./]#On[X, Y]

  type =!=[X <: Arity, Y <: Arity] = AssertEqual.On[X, Y]

  implicit class ArityInfix[X <: Arity](self: X) {

    def +[Y <: Arity](that: Y): X + Y = {

      Op2(self, that)
    }

    def -[Y <: Arity](that: Y): X - Y = {

      Op2(self, that)
    }

    def *[Y <: Arity](that: Y): X * Y = {

      Op2(self, that)
    }

    def /[Y <: Arity](that: Y): X / Y = {

      Op2(self, that)
    }
  }
}

object Syntax extends Syntax
