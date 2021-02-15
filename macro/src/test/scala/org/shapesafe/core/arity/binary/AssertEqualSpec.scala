package org.shapesafe.core.arity.binary

import com.tribbloids.graph.commons.util.viz.VizType
import org.shapesafe.core.arity
import org.shapesafe.core.arity.{Arity, ArityFixture, LeafArity}

import scala.language.existentials

class AssertEqualSpec extends ArityFixture {

  import org.shapesafe.core.arity.Syntax._

  describe("can prove") {

    describe("Arity.Const ==") {

      it("a") {

        val op = AssertEqual(a, a)
        op.proveLeaf.out.internal.requireEqual(3)
      }

      it("a + b") {

        val op = AssertEqual(a + b, ab)
        op.proveLeaf.out.internal.requireEqual(7)
      }

      it("a + b + c") {

        val op = AssertEqual(a + b + c, abc)
        op.proveLeaf.out.internal.requireEqual(12)
      }
    }

    describe("Op2 ==") {

      it("a + b + c") {

        val op = AssertEqual(a + b + c, ab + c)
        op.proveLeaf.out.internal.requireEqual(12)
      }
    }

    describe("Arity.Unknown ==") {

      it("a") {

        val op = AssertEqual(LeafArity.Unknown, a)

//        val impl = Implies.summon[a.type, Proof.Invar[a.SS]] _
//
//        UnsafeDomain.summon[Arity.Unknown.type, a.type]

        val out = op.proveLeaf.out
        assert(out.runtimeOpt.contains(3))

//        op.asProof.out.internal.requireEqual(3)
      }

      it("a + b") {

        val op = AssertEqual(a + b, LeafArity.Unknown)

        val out = op.proveLeaf.out
        assert(out.runtimeOpt.contains(7))
      }
    }
  }

  describe("CANNOT prove") {

    describe("<Operand Without Proof> ==") {

      it("a") {

        val op = AssertEqual(LeafArity.Unknown, a)

        shouldNotCompile(
          "op.asProof"
        )
      }

      it("a + b") {

        val op = AssertEqual(LeafArity.Unknown, a + b)

        shouldNotCompile(
          "op.asProof"
        )
      }
    }
  }
}
