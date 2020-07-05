package edu.umontreal.kotlingrad.shapesafe.m.arity.binary

import edu.umontreal.kotlingrad.shapesafe.m.arity.{Arity, AritySpecFixture, Implies, Proof}
import edu.umontreal.kotlingrad.shapesafe.m.util.debug.TypeView

class MayEqualSpec extends AritySpecFixture {

  describe("can prove") {

    describe("Arity.Const ==") {

      it("a") {

        val op = MayEqual(a, a)
        op.asProof.out.internal.requireEqual(3)
      }

      it("a + b") {

        val op = MayEqual(a + b, ab)
        op.asProof.out.internal.requireEqual(7)
      }

      it("a + b + c") {

        val op = MayEqual(a + b + c, abc)
        op.asProof.out.internal.requireEqual(12)
      }
    }

    describe("Op2 ==") {

      it("a + b + c") {

        val op = MayEqual(a + b + c, ab + c)
        op.asProof.out.internal.requireEqual(12)
      }
    }

    describe("Arity.Unknown ==") {

      it("a") {

        val op = MayEqual(Arity.Unknown, a)

//        val impl = Implies.summon[a.type, Proof.Invar[a.SS]] _
//
//        UnsafeDomain.summon[Arity.Unknown.type, a.type]

        val out = op.asProof.out
        assert(out.numberOpt.contains(3))

//        op.asProof.out.internal.requireEqual(3)
      }

      it("a + b") {

        val op = MayEqual(a + b, Arity.Unknown)

        val out = op.asProof.out
        assert(out.numberOpt.contains(7))
      }
    }
  }
}
