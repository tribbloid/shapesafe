package edu.umontreal.kotlingrad.shapesafe.m.arity

import edu.umontreal.kotlingrad.shapesafe.BaseSpec
import shapeless.Witness.Lt
import singleton.ops.{+, ==, Require, ToInt}

class AritySpec extends BaseSpec {

  import Arity._

  describe("big") {

    def validate[S](subject: Arity.Const[S], w: Lt[Int])(
        implicit proof: Require[S == w.T],
        plus: S + w.T
    ): Unit = {

      val out: Arity.Const[S] = subject.out
      val out2: Arity.Const[S] = subject.out.out

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
        val v = implicitly[FromOp[ToInt[big.nat.N]]]
        validate(v, 100)
      }

      {
        val v = implicitly[FromOp[ToInt[big.w.T]]]
        validate(v, 100)
      }
    }

    it("FromLiteral") {

      {
        val v = Arity(100)
        validate(v, 100)
      }

      {
        val v = implicitly[FromLiteral[big.w.T]]
        validate(v, 100)
      }
    }
  }
  // doesn't work at the moment
  //  it("OfIntLike") {
  //
  //    val v1: _ <: OfIntLike = 3
  //    println(v1)
  //  }

}
