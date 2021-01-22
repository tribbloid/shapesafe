package com.tribbloids.shapesafe.m.arity

import com.tribbloids.shapesafe.m.ProofSystem

import scala.language.higherKinds

/**
  * Represents a reified Arity
  */
object ProveArity extends ProofSystem[Arity] {

  trait Unsafe extends Out_=[Arity.Unsafe] {}

  trait Invar[S] extends Out_=[Arity.Static[S]] {

    type SS = S
  }
}
