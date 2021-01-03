package com.tribbloids.shapesafe.m.arity

import com.tribbloids.shapesafe.m.ProofSystem

import scala.language.higherKinds

/**
  * Represents a reified Arity
  */
object OfArity extends ProofSystem[Arity] {

  trait Unsafe extends Proof.Out_=[Arity.Unsafe] {}

  trait Invar[S] extends Proof.Out_=[Arity.Static[S]] {

    type SS = S
  }
}
