package org.shapesafe.core.arity

import org.shapesafe.core.arity.binary.{AssertEqual, Op2}
import singleton.ops

trait Syntax {

  type `+`[X <: Arity, Y <: Arity] = Op2[ops.+]#On[X, Y]

  type `-`[X <: Arity, Y <: Arity] = Op2[ops.-]#On[X, Y]

  type `*`[X <: Arity, Y <: Arity] = Op2[ops.*]#On[X, Y]

  type `/`[X <: Arity, Y <: Arity] = Op2[ops./]#On[X, Y]

  type =!=[X <: Arity, Y <: Arity] = AssertEqual.On[X, Y]

  implicit class ArityOps[X <: Arity](self: X) {

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

  // TODO: this should be more accurate
//  implicit class ArityOps[X <: Arity](val x: X) {
//
//    type THIS = x.type
//    val self: THIS = x: THIS
//
//    def +[Y <: Arity](that: Y): THIS + that.type = {
//
//      Op2(self, that)
//    }
//
//    def -[Y <: Arity](that: Y): THIS - that.type = {
//
//      Op2(self, that)
//    }
//
//    def *[Y <: Arity](that: Y): THIS * that.type = {
//
//      Op2(self, that)
//    }
//
//    def /[Y <: Arity](that: Y): THIS / that.type = {
//
//      Op2(self, that)
//    }
//  }
}

object Syntax extends Syntax
