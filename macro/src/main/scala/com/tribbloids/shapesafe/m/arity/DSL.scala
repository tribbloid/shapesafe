package com.tribbloids.shapesafe.m.arity

import com.tribbloids.shapesafe.m.arity.binary.Op2
import singleton.ops

object DSL {

  type T_+[X <: Operand, Y <: Operand] = Op2[X, Y, ops.+]

  type T_-[X <: Operand, Y <: Operand] = Op2[X, Y, ops.-]

  type T_*[X <: Operand, Y <: Operand] = Op2[X, Y, ops.*]

  type T_/[X <: Operand, Y <: Operand] = Op2[X, Y, ops./]

  implicit class OperandView[X <: Operand](it: X) {

    def +[
        Y <: Operand
    ](that: Y): X T_+ Y = {

      Op2(it, that)
    }

    def -[
        Y <: Operand
    ](that: Y): X T_- Y = {

      Op2(it, that)
    }

    def *[
        Y <: Operand
    ](that: Y): X T_* Y = {

      Op2(it, that)
    }

    def /[
        Y <: Operand
    ](that: Y): X T_/ Y = {

      Op2(it, that)
    }
  }
}
