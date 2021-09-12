package org.shapesafe.core

import org.shapesafe.Spike
import org.shapesafe.core.NatProofSpike.InductiveOn1
import org.shapesafe.core.fixtures.NatNumAxioms
import org.shapesafe.core.fixtures.NatNumAxioms.Nat

object NatProofSpike extends ProofSystem.^[Any] {
  import NatNumAxioms._

  trait InductiveOn1[T <: Nat]

  implicit def inductionAxiom[
      P1[R <: Nat] <: InductiveOn1[R],
      K <: Nat,
      FREE <: Nat
  ](
      implicit
      i1: P1[_0],
      i2: (FREE, P1[FREE]) |- P1[S[FREE]] // this should be a "polymorphic" proof
  ): K |- P1[K] = {
    forAll[K].=>>[P1[K]] { v =>
      ???
    }
  }

  case class ===[A <: Nat, B <: Nat]() {}
  implicit def proveEq[A <: Nat, B <: Nat](
      implicit
      ev: A =:= B
  ) = forAll[(A, B)].=>> { _ =>
    ===[A, B]()
  }
}

class NatProofSpike extends Spike {

  describe("+") {

    trait IsCommutative[A <: Nat, B <: Nat] extends InductiveOn1[A]

    //    implicit def

    object isCommutative {

      //      type theorem[K <: NN] = K
      //
      //      type Target[A <: NN, B <: NN] = (A + B) === (B + A)
    }

    object isAssociative {}

  }
}
