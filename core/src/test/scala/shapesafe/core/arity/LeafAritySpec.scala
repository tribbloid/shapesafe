package shapesafe.core.arity

import ai.acyclic.prover.commons.refl.XInt
import shapesafe.BaseSpec
import shapesafe.core.arity.ConstArity.{Derived, Literal}
import shapesafe.m.PeerType
import singleton.ops.{+, ==, Require, ToInt}

class LeafAritySpec extends BaseSpec {

  describe("big") {

    def validate[S, S2 <: XInt](subject: ConstArity[S], w: S2)(
        implicit
        proof: Require[S == S2],
        plus: S + S2
    ): Unit = {

      val out = subject
      val out2 = subject

      subject.proveEqual[S2](w)(proof)
      out.proveEqual[S2](w)(proof) // scala compiler may fumble on this one
      out2.proveEqual[S2](w)(proof)

      implicitly[subject.SS + S2]
      implicitly[out.SS + S2]
      implicitly[out2.SS + S2]
    }

    it("FromOp") {

      //    val v1 = implicitly[FromSize[SafeInt[big.nat.N]]]
      //    v1.core.requireEqual(100)

      {
        val v = Derived.summon[ToInt[big.nat.N]]
        validate(v, 100)
      }

      {
        val v = Derived.summon[ToInt[big.w.type]]
        validate(v, 100)
      }
    }

    it("FromLiteral") {

      {
        val v = Literal(100)
        validate(v, 100)
      }

      {
        val v = implicitly[Literal[big.w.type]]
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

  it("peek & interrupt") {

    val v1 = Arity(3)

    shouldNotCompile(
      """v1.interrupt""",
      """.*(3).*"""
    )
  }
}
