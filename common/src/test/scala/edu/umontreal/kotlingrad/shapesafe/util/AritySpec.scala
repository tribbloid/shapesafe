package edu.umontreal.kotlingrad.shapesafe.util

import edu.umontreal.kotlingrad.shapesafe.BaseSpec
import shapeless.{::, HNil, Nat, Witness}
import singleton.ops.{==, Require}

class AritySpec extends BaseSpec {

  import Arity._

  it("OfSize") {

    implicitly[Require[Nat._3 == Witness.`3`.T]] // just a sanity check

    val v = implicitly[OfSize[Int :: Int :: Int :: HNil, Nat._3]]

    v.internal.requireEqual(3)
  }

  it("OfInt") {

    val v1 = OfInt.safe(3)
    v1.internal.requireEqual(3)
  }

  // doesn't work at the moment
//  it("OfIntLike") {
//
//    val v1: _ <: OfIntLike = 3
//    println(v1)
//  }

}
