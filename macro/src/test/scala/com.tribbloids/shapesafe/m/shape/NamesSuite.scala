package com.tribbloids.shapesafe.m.shape

import com.tribbloids.graph.commons.util.viz.VizType
import com.tribbloids.shapesafe.BaseSpec
import shapeless.{HNil, Witness}

class NamesSuite extends BaseSpec {

  it("create") {

    import shapeless.syntax.singleton._

    val names = Names ~ "x" | "y" | "z"

    val expected = "z".narrow :: "y".narrow :: "x".narrow :: HNil

//    val expected = Witness("z").value :: Witness("y").value :: Witness("x").value :: HNil

    require(names.self == expected)

    val t1 = VizType.infer(names.self)
    val t2 = VizType.infer(expected)

    t1.toString shouldBe t2.toString
  }
}
