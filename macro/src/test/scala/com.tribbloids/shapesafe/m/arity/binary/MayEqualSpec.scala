package com.tribbloids.shapesafe.m.arity.binary

import com.tribbloids.shapesafe.m.arity.{Arity, Expression, ExpressionFixture}

import scala.language.existentials

class MayEqualSpec extends ExpressionFixture {

  import com.tribbloids.shapesafe.m.arity.DSL._

  describe("can prove") {

    describe("Arity.Const ==") {

      it("a") {

        val op = MayEqual(a, a)
        op.proveArity.out.internal.requireEqual(3)
      }

      it("a + b") {

        val op = MayEqual(a + b, ab)
        op.proveArity.out.internal.requireEqual(7)
      }

      it("a + b + c") {

        val op = MayEqual(a + b + c, abc)
        op.proveArity.out.internal.requireEqual(12)
      }
    }

    describe("Op2 ==") {

      it("a + b + c") {

        val op = MayEqual(a + b + c, ab + c)
        op.proveArity.out.internal.requireEqual(12)
      }
    }

    describe("Arity.Unknown ==") {

      it("a") {

        val op = MayEqual(Arity.Unknown, a)

//        val impl = Implies.summon[a.type, Proof.Invar[a.SS]] _
//
//        UnsafeDomain.summon[Arity.Unknown.type, a.type]

        val out = op.proveArity.out
        assert(out.numberOpt.contains(3))

//        op.asProof.out.internal.requireEqual(3)
      }

      it("a + b") {

        val op = MayEqual(a + b, Arity.Unknown)

        val out = op.proveArity.out
        assert(out.numberOpt.contains(7))
      }
    }
  }

  describe("CANNOT prove") {

    describe("<Operand Without Proof> ==") {

      it("a") {

        val op = MayEqual(Expression.Unprovable, a)

        shouldNotCompile(
          "op.asProof"
        )
      }

      it("a + b") {

        val op = MayEqual(Expression.Unprovable, a + b)

        shouldNotCompile(
          "op.asProof"
        )
      }
    }
  }
}
