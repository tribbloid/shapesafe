package org.shapesafe.core.arity

import org.shapesafe.BaseSpec

class AxisSpec extends BaseSpec {

  it("correct type") {

    val arity = Leaf.Literal(3)

    val dim = arity :<<- "abc"

    typeInferShort(dim)
      .shouldBe(
        """
          |Leaf.Literal[Int(3)] :<<- String("abc")""".stripMargin
      )
  }
}
