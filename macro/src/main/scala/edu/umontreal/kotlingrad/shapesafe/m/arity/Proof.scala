package edu.umontreal.kotlingrad.shapesafe.m.arity

import scala.language.higherKinds

/**
  * Represents a reified Arity
  */
trait Proof extends Serializable {

  def self: Operand

  type Out <: Arity

  def out: Out
}

object Proof {

  trait UnsafeLike extends Proof {}

  trait Unsafe extends UnsafeLike {

    final type Out = Arity.Unsafe
  }

  trait Out_=[+O <: Arity] extends Proof {
    type Out <: O
  }

  trait Invar[S] extends Out_=[Arity.Const[S]] {

    type SS = S
  }
}
