package edu.umontreal.kotlingrad.shapesafe.common.arity.nullary

import edu.umontreal.kotlingrad.shapesafe.BaseSpec
import shapeless.{::, HNil, Nat, Witness}
import singleton.ops.{==, Require, ToInt}

class OfSizeSpec extends BaseSpec {

  it("OfSize") {

    implicitly[Require[Nat._3 == Witness.`3`.T]] // just a sanity check

    val op = implicitly[OfSize[Int :: Int :: Int :: HNil, ToInt[Nat._3]]]

    val v = op.Out

    v.internal.requireEqual(3)
  }
}
