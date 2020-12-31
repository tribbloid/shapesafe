package com.tribbloids.shapesafe.m.arity

import com.tribbloids.shapesafe.m.Proof

import scala.language.higherKinds

/**
  * Represents a reified Arity
  */
trait ProofOfArity extends Proof {

  def in: Expression

  override type Out <: Arity
}

object ProofOfArity {

  type Lt[O <: Arity] = ProofOfArity {
    type Out <: O
  }

  trait Unsafe extends ProofOfArity {

    final type Out = Arity.Unsafe
  }

  // Can't use Aux Pattern due to several subclasses
  trait Out_=[O <: Arity] extends ProofOfArity {
    type Out = O
  }

  trait Invar[S] extends Out_=[Arity.Static[S]] {

    type SS = S
  }
}
