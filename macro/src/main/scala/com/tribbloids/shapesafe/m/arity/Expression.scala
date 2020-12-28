package com.tribbloids.shapesafe.m.arity

import com.tribbloids.shapesafe.m.~~>

// for "Expression"
trait Expression {

  final def proveArity[
      T >: this.type <: Expression,
      R <: ProofOfArity
  ](implicit prove: T ~~> R): R = prove.apply(this)

  final def proveArity_generic[
      T >: this.type <: Expression
  ](
      implicit prove: T ~~> ProofOfArity
  ): ProofOfArity = prove.apply(this)

  lazy val valueStr: String = "[??]"

  final override def toString: String = {

    this.getClass.getSimpleName + ": " + valueStr
  }
}

object Expression {

  object Unprovable extends Expression
}
