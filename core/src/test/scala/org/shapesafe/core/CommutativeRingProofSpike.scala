package org.shapesafe.core

import org.shapesafe.Spike
import org.shapesafe.core.fixtures.CommutativeRingAxioms
import org.shapesafe.core.fixtures.CommutativeRingAxioms.Stuff

class CommutativeRingProofSpike extends Spike {

  type A <: Stuff
  type B <: Stuff

  it("Sq[A :+ B]") {

    import CommutativeRingAxioms._

    Prove
      .forAll[Sq[A :+ B]]
      .withGoal[Sq[A] :+ Sq[B] :+ (A :* B) :+ (A :* B)]
//      .prove {
//
//        val input = null: Sq[A :+ B]
//
//        val out: A :+ B :* A :+ (A :+ B :* B) = :*.distributiveL.apply(input)
//
////        val fn = :*.distributiveL.summon[Sq[A :+ B]]
//
////        val v1 = Prove.forAll[Sq[B]].prove
////        :*.distributiveL[A :+ B, A, B]
//        ???
//      }
    // tactic mode!

  }
}

object CommutativeRingProofSpike {}
