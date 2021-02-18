package org.shapesafe.core.arity.ops

import org.shapesafe.core.arity.Arity
import org.shapesafe.core.arity.binary.{AssertEqual, Op2}
import singleton.ops

case class ArityOps[X <: Arity](self: X) {

  import org.shapesafe.core.arity.ops.ArityOps._

  def :+[Y <: Arity](that: Y): X :+ Y = {

    Op2(self, that)
  }

  def :-[Y <: Arity](that: Y): X :- Y = {

    Op2(self, that)
  }

  def :*[Y <: Arity](that: Y): X :* Y = {

    Op2(self, that)
  }

  def :/[Y <: Arity](that: Y): X :/ Y = {

    Op2(self, that)
  }
}

object ArityOps {

  type :+[X <: Arity, Y <: Arity] = Op2[ops.+]#On[X, Y]

  type :-[X <: Arity, Y <: Arity] = Op2[ops.-]#On[X, Y]

  type :*[X <: Arity, Y <: Arity] = Op2[ops.*]#On[X, Y]

  type :/[X <: Arity, Y <: Arity] = Op2[ops./]#On[X, Y]

  type =!=[X <: Arity, Y <: Arity] = AssertEqual.On[X, Y]
}
