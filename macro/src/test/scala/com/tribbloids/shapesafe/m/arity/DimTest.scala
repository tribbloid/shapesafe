package com.tribbloids.shapesafe.m.arity

import com.tribbloids.graph.commons.util.viz.VizType
import com.tribbloids.shapesafe.BaseSpec

class DimTest extends BaseSpec {

  it("correct type") {

    val arity = Arity.FromLiteral(3)

    val dim = arity :<<- "abc"

    VizType
      .infer(dim)
      .tree
      .typeStr
      .shouldBe(
        """com.tribbloids.shapesafe.m.arity.Arity.FromLiteral[Int(3)] :<<- String("abc")"""
      )
  }
}
