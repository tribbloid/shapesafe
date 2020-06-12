package edu.umontreal.kotlingrad.shapesafe.m.arity

import edu.umontreal.kotlingrad.shapesafe.m.arity.binary.Op2
import singleton.ops

object DSL {

  type Plus[X <: Operand, Y <: Operand] = Op2[X, Y, ops.+]

  type Minus[X <: Operand, Y <: Operand] = Op2[X, Y, ops.-]

  type Times[X <: Operand, Y <: Operand] = Op2[X, Y, ops.*]

  type DividedBy[X <: Operand, Y <: Operand] = Op2[X, Y, ops./]

  implicit class OperandView[X <: Operand](it: X) {

    def +[
        Y <: Operand
    ](that: Y): X Plus Y = {

      Op2(it, that)
    }

    def -[
        Y <: Operand
    ](that: Y): X Minus Y = {

      Op2(it, that)
    }

    def *[
        Y <: Operand
    ](that: Y): X Times Y = {

      Op2(it, that)
    }

    def /[
        Y <: Operand
    ](that: Y): X DividedBy Y = {

      Op2(it, that)
    }
  }
}
