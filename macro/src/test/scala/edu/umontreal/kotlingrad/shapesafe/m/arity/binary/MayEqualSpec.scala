package edu.umontreal.kotlingrad.shapesafe.m.arity.binary

import edu.umontreal.kotlingrad.shapesafe.BaseSpec
import edu.umontreal.kotlingrad.shapesafe.m.arity.Arity

class MayEqualSpec extends BaseSpec {

  describe("can prove") {

    it("same Const") {

      val a = Arity(7)

      val op = MayEqual(a, a)
      op.asProof.out.internal.requireEqual(7)
    }
  }
}
