package org.shapesafe.core.arity

import org.shapesafe.core.arity.binary.Op2
import singleton.ops

trait Syntax {

  type `+`[X <: Arity, Y <: Arity] = Op2[X, Y, ops.+]

  type `-`[X <: Arity, Y <: Arity] = Op2[X, Y, ops.-]

  type `*`[X <: Arity, Y <: Arity] = Op2[X, Y, ops.*]

  type `/`[X <: Arity, Y <: Arity] = Op2[X, Y, ops./]

  implicit class ArityView[X <: Arity](it: X) {

    def +[Y <: Arity](that: Y): X `+` Y = {

      Op2(it, that)
    }

    def -[Y <: Arity](that: Y): X `-` Y = {

      Op2(it, that)
    }

    def *[Y <: Arity](that: Y): X `*` Y = {

      Op2(it, that)
    }

    def /[Y <: Arity](that: Y): X `/` Y = {

      Op2(it, that)
    }
  }
}

object Syntax extends Syntax
