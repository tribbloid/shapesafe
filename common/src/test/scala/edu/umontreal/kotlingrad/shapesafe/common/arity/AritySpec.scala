package edu.umontreal.kotlingrad.shapesafe.common.arity

import edu.umontreal.kotlingrad.shapesafe.BaseSpec
import shapeless.{Nat, Witness}
import singleton.ops.ToInt

class AritySpec extends BaseSpec {

  import Arity._

  it("FromSize") {

    val v1 = implicitly[FromSize[ToInt[Nat._3]]]
    v1.internal.requireEqual(3)
  }

  it("FromLiteral") {

    val v1 = FromLiteral.create(3)
    v1.internal.requireEqual(3)

    val v2 = implicitly[FromLiteral[Witness.`3`.T]]
    v2.internal.requireEqual(3)
  }

  // doesn't work at the moment
//  it("OfIntLike") {
//
//    val v1: _ <: OfIntLike = 3
//    println(v1)
//  }

}
