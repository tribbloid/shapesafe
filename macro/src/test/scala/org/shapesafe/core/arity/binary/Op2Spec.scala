package org.shapesafe.core.arity.binary

import org.shapesafe.core.arity.{Arity, ArityFixture, Leaf}

class Op2Spec extends ArityFixture {

  import org.shapesafe.core.arity.Syntax._

  describe("can prove") {

    it("arity trivially") {

      val p = a.proveLeaf
      p.out.internal.requireEqual(3)
    }

    it("a + b") {

      val op = a + b

      val p = op.proveLeaf
      p.out.internal.requireEqual(7)
    }

    it("a + b + c") {

      val op0 = a + b
      val op = op0 + c

      val p = op.proveLeaf
      p.out.internal.requireEqual(12)
    }

    it("... in 1 line") {

      val op = a + b + c

      val p = op.proveLeaf
      p.out.internal.requireEqual(12)
    }

    it("a + b + c + d") {

      val op = a + b + c + Leaf._1.value

      val p = op.proveLeaf
      p.out.internal.requireEqual(13)
    }

    it("b / a") {

      val op = b / a

      val p = op.proveLeaf
      p.out.internal.requireEqual(1)
    }

    it("... NOT if b == 0") {

      val op = a / Leaf._0.value

      shouldNotCompile {
        "val p = op.asProof"
      }
    }

    it("(a + b - c) / d") {

      val op = (a + b - c) / Leaf._1.value

      val p = op.proveLeaf
      p.out.internal.requireEqual(2)
    }
  }

  describe("CANNOT prove") {

    describe("<Operand Without Proof> +") {

      it("a") {

        val op = Leaf.Unknown + a

        shouldNotCompile(
          "op.asProof"
        )
      }

      it("a + b") {

        val op = Leaf.Unknown + (a + b)

        shouldNotCompile(
          "op.asProof"
        )
      }
    }
  }
}
