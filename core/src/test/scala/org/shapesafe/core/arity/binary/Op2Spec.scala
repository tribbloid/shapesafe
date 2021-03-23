package org.shapesafe.core.arity.binary

import org.shapesafe.core.arity.{Arity, ArityAPI, ArityFixture, LeafArity}

class Op2Spec extends ArityFixture {

  describe("can prove") {

    it("arity trivially") {

      val p = a.eval
      p.requireEqual(3)
    }

    it("a + b") {

      val op = a :+ b

      val p = op.eval
      p.requireEqual(7)
    }

    it("a + b + c") {

      val op0 = a :+ b
      val op = op0 :+ c

      val p = op.eval
      p.requireEqual(12)
    }

    it("... in 1 line") {

      val op = a :+ b :+ c

      val p = op.eval
      p.requireEqual(12)
    }

    it("a + b + c + d") {

      val op = a :+ b :+ c :+ Arity._1

      val p = op.eval
      p.requireEqual(13)
    }

    it("b / a") {

      val op = b :/ a

      val p = op.eval
      p.requireEqual(1)
    }

    it("... NOT if b == 0") {

      val op = a :/ Arity._0

      shouldNotCompile {
        "val p = op.asProof"
      }
    }

    it("(a + b - c) / d") {

      val op = (a :+ b :- c) :/ Arity._1

      val p = op.eval
      p.requireEqual(2)
    }
  }

  describe("CANNOT prove") {

    describe("<Operand Without Proof> +") {

      it("a") {

        val op = LeafArity.Unchecked.^ :+ a

        shouldNotCompile(
          "op.asProof"
        )
      }

      it("a + b") {

        val op = LeafArity.Unchecked.^ :+ (a :+ b)

        shouldNotCompile(
          "op.asProof"
        )
      }
    }
  }
}
