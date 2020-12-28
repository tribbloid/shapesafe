package com.tribbloids.shapesafe.m.arity

import com.tribbloids.shapesafe.m.Proof

import scala.language.higherKinds

/**
  * Represents a reified Arity
  */
trait ProofOfArity extends Proof {

  override type Out <: Arity
}

object ProofOfArity {

  trait UnsafeLike extends ProofOfArity {}

  trait Unsafe extends UnsafeLike {

    final type Out = Arity.Unsafe
  }

  trait Out_=[+O <: Arity] extends ProofOfArity {
    type Out <: O
  }

  trait Invar[S] extends Out_=[Arity.Static[S]] {

    type SS = S
  }
}
