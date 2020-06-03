package edu.umontreal.kotlingrad.shapesafe

import shapeless.HNil

import scala.util.Random

class DoubleVectorSpec extends BaseSpec {

  it("from tuple") {

    val v = DoubleVector.fromHList(1.0 :: 2.0 :: 3.0 :: HNil)

    assert(v.arity.number == 3)
    v.arity.proveEqual_internal[W.`3`.T]
  }

  it("from literal") {

    val v = DoubleVector.zeros(3)

    assert(v.arity.number == 3)
    v.arity.proveEqual_internal[W.`3`.T]
  }

  it("from non-literal") {

    val v = DoubleVector.zeros(Random.nextInt(5))

    assert(v.arity == Arity.??)
  }
}
