package org.shapesafe.core.arity

import com.tribbloids.graph.commons.util.viz.TypeViz
import org.shapesafe.BaseSpec
import org.shapesafe.m.PeerType
import shapeless.Witness.Lt
import singleton.ops.{+, ==, Require, ToInt}

class LeafAritySpec extends BaseSpec {

  import LeafArity._

  describe("big") {

    def validate[S](subject: LeafArity.Const[S], w: Lt[Int])(
        implicit
        proof: Require[S == w.T],
        plus: S + w.T
    ): Unit = {

      val out = subject
      val out2 = subject

      subject.requireEqual(w)(proof)
      out.requireEqual(w)(proof) // scala compiler may fumble on this one
      out2.requireEqual(w)(proof)

      implicitly[subject.SS + w.T]
      implicitly[out.SS + w.T]
      implicitly[out2.SS + w.T]
    }

    it("FromOp") {

      //    val v1 = implicitly[FromSize[SafeInt[big.nat.N]]]
      //    v1.core.requireEqual(100)

      {
        val v = implicitly[Derived[ToInt[big.nat.N]]]
        validate(v, 100)
      }

      {
        val v = implicitly[Derived[ToInt[big.w.T]]]
        validate(v, 100)
      }
    }

    it("FromLiteral") {

      {
        val v = Literal(100)
        validate(v, 100)
      }

      {
        val v = implicitly[Literal[big.w.T]]
        validate(v, 100)
      }
    }
  }

  it("eval") {

    val v = Arity(3)
    TypeViz.infer(v.eval) ===! TypeViz.infer(v)
  }

  it("verify") {

    val v = Arity(3)
    TypeViz.infer(v.verify) ===! TypeViz.infer(v)
  }

  describe("has PeerType") {

    val a = Arity(3)
    val gt = TypeViz.infer(a)

    it("2") {
      val v2 = {
        val v = PeerType[a.type]
        TypeViz[v.Out]
      }

      v2.should_=:=(gt)
    }
  }

  // doesn't work at the moment
  it("toString") {

    val v1 = Arity(3)
    v1.toString.shouldBe("3:Literal")
  }

  it("peek") {

    val v1 = Arity(3)

    shouldNotCompile(
      """v1.peek""",
      """.*(3)"""
    )

  }
}
