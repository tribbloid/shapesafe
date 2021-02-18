package org.shapesafe.core.arity

import org.shapesafe.BaseSpec

class AxisSpec extends BaseSpec {

  it("correct type") {

    val arity = LeafArity.Literal(3)

    val dim = arity :<<- "abc"

    typeInferShort(dim)
      .shouldBe(
        """
          |LeafArity.Literal[Int(3)] :<<- String("abc")""".stripMargin
      )
  }
}