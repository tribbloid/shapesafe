package org.shapesafe.core.arity.binary

import org.shapesafe.core.arity.{Arity, ArityFixture, LeafArity}

class Op2Spec extends ArityFixture {

  import org.shapesafe.core.arity.Syntax._

  describe("can prove") {

    it("arity trivially") {

      val p = a.eval
      p.internal.requireEqual(3)
    }

    it("a + b") {

      val op = a + b

      val p = op.eval
      p.internal.requireEqual(7)
    }

    it("a + b + c") {

      val op0 = a + b
      val op = op0 + c

      val p = op.eval
      p.internal.requireEqual(12)
    }

    it("... in 1 line") {

      val op = a + b + c

      val p = op.eval
      p.internal.requireEqual(12)
    }

    it("a + b + c + d") {

      val op = a + b + c + LeafArity._1

      val p = op.eval
      p.internal.requireEqual(13)
    }

    it("b / a") {

      val op = b / a

      val p = op.eval
      p.internal.requireEqual(1)
    }

    it("... NOT if b == 0") {

      val op = a / LeafArity._0

      shouldNotCompile {
        "val p = op.asProof"
      }
    }

    it("(a + b - c) / d") {

      val op = (a + b - c) / LeafArity._1

      val p = op.eval
      p.internal.requireEqual(2)
    }
  }

  describe("CANNOT prove") {

    describe("<Operand Without Proof> +") {

      it("a") {

        val op = LeafArity.Unchecked + a

        shouldNotCompile(
          "op.asProof"
        )
      }

      it("a + b") {

        val op = LeafArity.Unchecked + (a + b)

        shouldNotCompile(
          "op.asProof"
        )
      }
    }
  }
}
