package edu.umontreal.kotlingrad.shapesafe.m.arity

import edu.umontreal.kotlingrad.shapesafe.BaseSpec
import shapeless.Witness
import singleton.ops.ToInt

class AritySpec extends BaseSpec {

  import Arity._

  it("FromOp") {

    //    val v1 = implicitly[FromSize[SafeInt[big.nat.N]]]
    //    v1.internal.requireEqual(100)

    val v2 = implicitly[FromOp[ToInt[big.nat.N]]]
    v2.internal.requireEqual(100)

    val v3 = implicitly[FromOp[ToInt[big.w.T]]]
    v3.internal.requireEqual(100)
  }

  it("FromLiteral") {

    val v1 = Arity(100)
    v1.internal.requireEqual(100)

    val v2 = implicitly[FromLiteral[big.w.T]]
    v2.internal.requireEqual(100)
  }

  // doesn't work at the moment
  //  it("OfIntLike") {
  //
  //    val v1: _ <: OfIntLike = 3
  //    println(v1)
  //  }

}
