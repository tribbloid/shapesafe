package edu.umontreal.kotlingrad.shapesafe.m.arity.nullary

import edu.umontreal.kotlingrad.shapesafe.m.arity.AritySpecFixture
import shapeless.{::, HNil, Nat}
import singleton.ops.{==, Require}

class OfSizeSpec extends AritySpecFixture {

  describe("observing HList") {

    it("small") {

      val op0 = OfSize.observe[Int :: Int :: Int :: HNil, Nat._3]
      op0.out.internal.requireEqual(3)

      val hList = 0 :: 1 :: 2 :: HNil
      val op1 = OfSize.observe(hList)
      op1.out.internal.requireEqual(3)
    }

    it("big") {

      implicitly[Require[big.nat.N == big.w.T]] // just a sanity check

      val op1 = OfSize.observe(big.hList)
      op1.out.internal.requireEqual(100)
    }
  }
}
