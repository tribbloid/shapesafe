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

    // Can't use Aux Pattern due to several subclasses
    trait Out_=[O <: UB] extends Proof {
      final type Out = O
    }
  }
}
