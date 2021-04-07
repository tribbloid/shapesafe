package org.shapesafe.core.arity.binary

import org.shapesafe.core.arity.{Arity, ArityFixture, LeafArity}

import scala.language.existentials

class AssertEqualSpec extends ArityFixture {

  describe("can prove") {

    describe("Arity.Const ==") {

      it("a") {

        val op = AssertEqual.on(a, a).^
        op.eval.requireEqual(3)
      }

      it("a + b") {

        val op = AssertEqual.on(a :+ b, ab).^
        op.eval.requireEqual(7)
      }

      it("a + b + c") {

        val op = AssertEqual.on(a :+ b :+ c, abc).^
        op.eval.requireEqual(12)
      }
    }

    describe("Op2 ==") {

      it("a + b + c") {

        val op = AssertEqual.on(a :+ b :+ c, ab :+ c).^
        op.eval.requireEqual(12)
      }
    }

    describe("Arity.Unchecked ==") {

      it("a") {

        val op = AssertEqual.on(LeafArity.Unchecked.^, a).^

        val out = op.eval
        out.requireEqual(3)

//        op.asProof.out.core.requireEqual(3)
      }

      it("a + b") {

        val sum = a :+ b
        val op = AssertEqual.on(sum, LeafArity.Unchecked.^).^

        val out = op.eval
        out.requireEqual(7)
      }
    }
  }

  describe("CANNOT prove") {

    describe("Unprovable == ?") {

      it("1") {

        val op = AssertEqual.on(Arity.Unprovable.^, a).^

        shouldNotCompile(
          "op.eval",
//          ".*(Arity.Unprovable.type != 3)"// TODO: doesn't work until fallback mechanism is implemented
          ".*"
        )
      }

      it("2") {

        val op = AssertEqual.on(Arity.Unprovable.^, a :+ b).^

        shouldNotCompile(
          "op.eval",
//          ".*(Arity.Unprovable.type != 7)" // TODO: doesn't work until fallback mechanism is implemented
          ".*"
        )
      }
    }

    describe("if a != b") {

      it("1") {

        val op = AssertEqual.on(a, c).^

        shouldNotCompile(
          "op.eval",
          """.*(3 != 5)"""
        )
      }

      it("2") {

        val op = AssertEqual.on(a, b :+ c).^

        shouldNotCompile(
          "op.eval",
          """.*(3 != 9)"""
        )
      }
    }
  }
}
