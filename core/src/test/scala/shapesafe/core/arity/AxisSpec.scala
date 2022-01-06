package shapesafe.core.arity

import shapesafe.BaseSpec

class AxisSpec extends BaseSpec {

  it("correct type") {

    val arity = Arity(3)

    val dim = arity :<<- "abc"

    typeInferShort(dim)
      .shouldBe(
        """
          |ConstArity.Literal[Int(3)] :<<- String("abc")""".stripMargin
      )
  }
}
