package edu.umontreal.kotlingrad.shapesafe.m.arity

/**
  * is a view class
  */
trait Proof extends Serializable {

  def self: Operand

  type Out <: Arity

  def out: Out
}

object Proof {

  type Aux[T] = Proof { type Out = T }
  type Lt[T] = Proof { type Out <: T }

  trait Unsafe extends Proof {

    final type Out = Arity.Unknown.type
    final def out: Out = Arity.Unknown
  }

  // TODO: type Out should be a parameter instead of a dependent type
  //  otherwise inferring Out will require Lazy
  trait Invar extends Proof {
    type Out <: Arity.Const[_]
  } // can't use type alias? really?
}
