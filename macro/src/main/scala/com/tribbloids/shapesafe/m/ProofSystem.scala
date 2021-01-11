package com.tribbloids.shapesafe.m

trait ProofSystem[UB] {

  trait Proof extends Serializable {

    type Out <: UB

    def out: Out
  }

  case object Proof {

    type Aux[O <: UB] = Proof {
      type Out = O
    }

    type Lt[O <: UB] = Proof {
      type Out <: O
    }

  }

  // Can't use Aux, syntax not supported by scala
  trait Out_=[O <: UB] extends Proof {
    final type Out = O
  }

  // doesn't extend T => R intentionally
  // each ProofSystem use a different one to alleviate search burden of compiler (or is it redundant?)
  trait ~~>[-T, +R] {
    def apply(v: T): R
  }

  object ~~> {

    implicit def summon[T, R](v: T)(implicit bound: T ~~> R): R = bound.apply(v)

    implicit def trivial[T]: T ~~> T = identity
  }

}
