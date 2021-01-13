package com.tribbloids.shapesafe.m.shape.nullary

import com.tribbloids.shapesafe.BaseSpec
import com.tribbloids.shapesafe.m.arity.Arity
import com.tribbloids.shapesafe.m.shape.Shape
import shapeless.HNil

class OfStaticTest extends BaseSpec {

  import shapeless.syntax.singleton.mkSingletonOps

  it("from HNil") {

    val hh = HNil

    val shape = OfStatic.observe(hh)

    assert(shape == Shape.Eye)
  }

  it("1") {

    val hh = ("x" ->> Arity(3)) ::
      HNil

    val shape = OfStatic.observe(hh)

//    VizType.infer(shape).toString.shouldBe()

    assert(shape.static == hh)
  }

  it("2") {

    val hh = ("x" ->> Arity(3)) ::
      ("y" ->> Arity(4)) ::
      HNil

    val shape = OfStatic.observe(hh)

//    VizType.infer(shape).toString.shouldBe()

    assert(shape.static == hh)
  }
}
