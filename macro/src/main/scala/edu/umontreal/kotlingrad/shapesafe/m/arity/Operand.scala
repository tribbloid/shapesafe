package edu.umontreal.kotlingrad.shapesafe.m.arity

import edu.umontreal.kotlingrad.shapesafe.m.arity.binary.Op2
import singleton.ops._

// for "Expression"
trait Operand {

  def +[
      X >: this.type <: Operand,
      Y <: Operand
  ](that: Y): Op2[X, Y, +] = {

    Op2[X, Y, +](this, that)
  }

  def -[
      X >: this.type <: Operand,
      Y <: Operand
  ](that: Y): Op2[X, Y, -] = {

    Op2[X, Y, -](this, that)
  }

  def *[
      X >: this.type <: Operand,
      Y <: Operand
  ](that: Y): Op2[X, Y, *] = {

    Op2[X, Y, *](this, that)
  }

  def /[
      X >: this.type <: Operand,
      Y <: Operand
  ](that: Y): Op2[X, Y, /] = {

    Op2[X, Y, /](this, that)
  }

  final def asProof[
      T >: this.type <: Operand,
      R <: Proof
  ](implicit prove: T Implies R): R = prove.apply(this)

  final def asProof_generic[
      T >: this.type <: Operand
  ](
      implicit prove: T Implies Proof
  ): Proof = prove.apply(this)
}

object Operand {}
