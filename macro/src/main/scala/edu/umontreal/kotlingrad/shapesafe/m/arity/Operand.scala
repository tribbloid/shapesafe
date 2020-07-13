package edu.umontreal.kotlingrad.shapesafe.m.arity

import edu.umontreal.kotlingrad.shapesafe.m.~~>

// for "Expression"
trait Operand {

  final def asProof[
      T >: this.type <: Operand,
      R <: Proof
  ](implicit prove: T ~~> R): R = prove.apply(this)

  final def asProof_generic[
      T >: this.type <: Operand
  ](
      implicit prove: T ~~> Proof
  ): Proof = prove.apply(this)

  lazy val valueStr: String = "[??]"

  final override def toString: String = {

    this.getClass.getSimpleName + ": " + valueStr
  }
}

object Operand {

  private[arity] object Unprovable extends Operand
}
