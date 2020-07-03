package edu.umontreal.kotlingrad.shapesafe.m.arity.binary

import edu.umontreal.kotlingrad.shapesafe.BaseSpec
import edu.umontreal.kotlingrad.shapesafe.m.arity.Arity
import edu.umontreal.kotlingrad.shapesafe.m.util.debug.{DebugUtils, TypeView}

class Op2Spec extends BaseSpec {

  describe("can prove") {

    implicit val a = Arity(3)
    type A = a.type

    implicit val b = Arity(4)
    type B = b.type

    implicit val c = Arity(5)
    type C = c.type

    it("arity itself") {

      val p = a.asProof
      p.out.internal.requireEqual(3)
    }

    it("a + b") {

      val op = a + b

      val p = op.asProof
      p.out.internal.requireEqual(7)
    }

    it("a + b + c") {

      val op0 = a + b
      val op = op0 + c

      val p = op.asProof
      p.out.internal.requireEqual(12)
    }

    it("... in 1 line") {

      val op = a + b + c

      val p = op.asProof
      p.out.internal.requireEqual(12)
    }

    it("a + b + c + d") {

      val op = a + b + c + Arity._1

      val p = op.asProof
      p.out.internal.requireEqual(13)
    }

    it("b / a") {

      val op = b / a

      val p = op.asProof
      p.out.internal.requireEqual(1)
    }

    it("... NOT if b == 0") {

      val op = a / Arity._0

      shouldNotCompile {
        "val p = op.asProof"
      }
    }

    it("(a + b - c) / d") {

      val op = (a + b - c) / Arity._1

      val p = op.asProof
      p.out.internal.requireEqual(2)
    }
  }
}
