package edu.umontreal.kotlingrad.shapesafe.`macro`.arity

trait Proof extends Serializable {

  type Out <: Arity
  def out: Out
}

object Proof {

  type Aux[T] = Proof { type Out = T }
  type Lt[T] = Proof { type Out <: T }

  type Unsafe = Aux[Arity.Unknown.type]

  trait Invar[S] extends Proof { type Out <: Arity.Const[S] } // can't use type alias? really?
}
