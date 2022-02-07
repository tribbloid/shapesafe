package shapesafe.core.arity.binary

import shapesafe.core.arity.ops.ArityOpsLike.RequireEqual
import shapesafe.core.arity.{Arity, ArityFixture, Unchecked}

class Require2Spec extends ArityFixture {

  describe("can prove") {

    describe("Arity.Const ==") {

      it("a") {

        val op = RequireEqual.on(a, a).^
        op.eval.proveEqual(3)
      }

      it("a + b") {

        val op = RequireEqual.on(a :+ b, ab).^
        op.eval.proveEqual(7)
      }

      it("a + b + c") {

        val op = RequireEqual.on(a :+ b :+ c, abc).^
        op.eval.proveEqual(12)
      }
    }

    describe("Op2 ==") {

      it("a + b + c") {

        val op = RequireEqual.on(a :+ b :+ c, ab :+ c).^
        op.eval.proveEqual(12)
      }
    }

    describe("Arity.Unchecked ==") {

      it("a") {

        val op = RequireEqual.on(Unchecked.^, a).^

        val out = op.eval
        out.proveEqual(3)

//        op.asProof.out.core.proveEqual(3)
      }

      it("a + b") {

        val sum = a :+ b
        val op = RequireEqual.on(sum, Unchecked.^).^

        val out = op.eval
        out.proveEqual(7)
      }
    }
  }

  describe("CANNOT prove") {

    describe("Unprovable == ?") {

      it("1") {

        val op = RequireEqual.on(Arity.Unprovable, a).^

        shouldNotCompile(
          "op.eval",
//          ".*(Arity.Unprovable.type != 3)"// TODO: doesn't work until fallback mechanism is implemented
          ".*"
        )
      }

      it("2") {

        val op = RequireEqual.on(Arity.Unprovable, a :+ b).^

        shouldNotCompile(
          "op.eval",
//          ".*(Arity.Unprovable.type != 7)" // TODO: doesn't work until fallback mechanism is implemented
          ".*"
        )
      }
    }

    describe("a == b if not") {

      it("1") {

        val op = RequireEqual.on(a, c).^

        shouldNotCompile(
          "op.eval",
          """.*(3 != 5).*"""
        )
      }

      it("2") {

        val op = RequireEqual.on(a, b :+ c).^

        shouldNotCompile(
          "op.eval",
          """.*(3 != 9).*"""
        )
      }
    }
  }
}
