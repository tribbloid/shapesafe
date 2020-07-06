package edu.umontreal.kotlingrad.shapesafe.m.arity

import edu.umontreal.kotlingrad.shapesafe.m.arity.binary.Op2
import singleton.ops

object DSL {

  type +[X <: Operand, Y <: Operand] = Op2[X, Y, ops.+]

  type -[X <: Operand, Y <: Operand] = Op2[X, Y, ops.-]

  type *[X <: Operand, Y <: Operand] = Op2[X, Y, ops.*]

  type /[X <: Operand, Y <: Operand] = Op2[X, Y, ops./]

  implicit class OperandView[X <: Operand](it: X) {

    def +[
        Y <: Operand
    ](that: Y): X + Y = {

      Op2(it, that)
    }

    def -[
        Y <: Operand
    ](that: Y): X - Y = {

      Op2(it, that)
    }

    def *[
        Y <: Operand
    ](that: Y): X * Y = {

      Op2(it, that)
    }

    def /[
        Y <: Operand
    ](that: Y): X / Y = {

      Op2(it, that)
    }
  }
}
