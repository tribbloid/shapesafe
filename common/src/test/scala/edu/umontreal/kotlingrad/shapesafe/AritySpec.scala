package edu.umontreal.kotlingrad.shapesafe

import shapeless.{::, HNil, Nat}
import singleton.ops.{==, Require}

class AritySpec extends BaseSpec {

  import Arity._

  it("FromNat") {

    implicitly[Require[Nat._3 == W.`3`.T]] // just a sanity check

    val v = implicitly[OfSize[Int :: Int :: Int :: HNil, Nat._3]]

    v.proveEqual_internal[W.`3`.T]
  }

  it("FromLiteral") {

    val v1 = FromLiteral.make(3)
    v1.proveEqual_internal[W.`3`.T]

    // TODO: this doesn't compile
//    val v2: FromNumber[W.`3`.T] = FromNumber.create(3)
//    println(v2.number)

//    v2.proveEqualInternal[W.`3`.T]
  }
}
