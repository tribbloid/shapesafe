package org.shapesafe.core.arity.nullary

import org.shapesafe.core.arity.ArityFixture
import shapeless.HNil
import singleton.ops.{==, Require}

class SizeOfSpec extends ArityFixture {

  describe("observing HList") {

    it("small") {

      val op0 = SizeOf(1 :: 2 :: 3 :: HNil)
      val proven = op0.eval

      proven.internal.requireEqual(3)

      val hList = 0 :: 1 :: 2 :: HNil
      val op1 = SizeOf.getConst(hList)
      op1.internal.requireEqual(3)
    }

    it("big") {

      implicitly[Require[big.nat.N == big.w.T]] // just a sanity check

      val op1 = SizeOf.getConst(big.hList)
      op1.internal.requireEqual(100)
    }
  }
}
