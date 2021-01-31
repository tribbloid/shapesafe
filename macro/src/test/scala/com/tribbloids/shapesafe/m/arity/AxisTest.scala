package com.tribbloids.shapesafe.m.arity

import com.tribbloids.graph.commons.util.viz.VizType
import com.tribbloids.shapesafe.BaseSpec

class AxisTest extends BaseSpec {

  it("correct type") {

    val arity = Arity.Literal(3)

    val dim = arity :<<- "abc"

    typeInferShort(dim)
      .shouldBe(
        """
          |Arity.Literal[Int(3)] :<<- String("abc")""".stripMargin
      )
  }
}
