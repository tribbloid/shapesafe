package org.shapesafe.core.arity.binary

import org.shapesafe.core.arity.{Arity, ArityFixture, Unchecked}

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

        val op = Unchecked.^ :+ a

        shouldNotCompile(
          "op.asProof"
        )
      }

      it("a + b") {

        val op = Unchecked.^ :+ (a :+ b)

        shouldNotCompile(
          "op.asProof"
        )
      }
    }
  }

  describe("peek & interrupt") {

    it("1") {

//      (b :+ c).peek

      shouldNotCompile(
        """(b :+ c).interrupt""",
        """.*(\Q  :=  4 + 5\E).*"""
      )
    }

    // TODO: doesn't work until fallback mechanism is implemented
    it("2") {

      shouldNotCompile(
        """(Arity.Unprovable :+ c).interrupt""",
        """.*(_UNPROVABLE_ \+ 5).*"""
      )
    }
  }
}
