package org.shapesafe.core.arity

import org.shapesafe.BaseSpec
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

      val out = subject.out
      val out2 = subject.out.out

      subject.internal.requireEqual(w)(proof)
      out.internal.requireEqual(w)(proof) // scala compiler may fumble on this one
      out2.internal.requireEqual(w)(proof)

      implicitly[subject.SS + w.T]
      implicitly[out.SS + w.T]
      implicitly[out2.SS + w.T]
    }

    it("FromOp") {

      //    val v1 = implicitly[FromSize[SafeInt[big.nat.N]]]
      //    v1.internal.requireEqual(100)

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
        val v = LeafArity(100)
        validate(v, 100)
      }

      {
        val v = implicitly[Literal[big.w.T]]
        validate(v, 100)
      }
    }
  }
  // doesn't work at the moment
  it("OfIntLike") {

    val v1 = LeafArity(3)
    v1.toString.shouldBe("3:Literal")
  }
}
