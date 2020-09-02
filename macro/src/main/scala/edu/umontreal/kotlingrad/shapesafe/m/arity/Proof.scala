package edu.umontreal.kotlingrad.shapesafe.m.arity

import scala.language.higherKinds

/**
  * Represents a reified Arity
  */
trait Proof extends Serializable {

  type Out <: Arity
  def out: Out
}

object Proof {

  trait Invar[S] extends Proof {

    type Out <: Arity.Const[S]

    type SS = S
  }
}
