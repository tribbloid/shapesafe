package edu.umontreal.kotlingrad.shapesafe

import shapeless.HNil

class DoubleVectorSpec extends BaseSpec {

  it("from tuple") {

    val v = DoubleVector.fromHList(1.0 :: 2.0 :: 3.0 :: HNil)

    assert(v.arity.value == 3)
  }
}
