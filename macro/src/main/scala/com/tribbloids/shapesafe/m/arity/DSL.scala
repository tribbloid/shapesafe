package com.tribbloids.shapesafe.m.arity

import com.tribbloids.shapesafe.m.arity.binary.Expr2
import singleton.ops

object DSL {

  type `+`[X <: Expression, Y <: Expression] = Expr2[X, Y, ops.+]

  type `-`[X <: Expression, Y <: Expression] = Expr2[X, Y, ops.-]

  type `*`[X <: Expression, Y <: Expression] = Expr2[X, Y, ops.*]

  type `/`[X <: Expression, Y <: Expression] = Expr2[X, Y, ops./]

  implicit class ExpressionView[X <: Expression](it: X) {

    def +[Y <: Expression](that: Y): X `+` Y = {

      Expr2(it, that)
    }

    def -[Y <: Expression](that: Y): X `-` Y = {

      Expr2(it, that)
    }

    def *[Y <: Expression](that: Y): X `*` Y = {

      Expr2(it, that)
    }

    def /[Y <: Expression](that: Y): X `/` Y = {

      Expr2(it, that)
    }
  }
}
