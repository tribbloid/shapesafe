package shapesafe.core.axis

import shapesafe.BaseSpec
import shapesafe.core.arity.Arity

class AxisSpec extends BaseSpec {

  it("correct type") {

    val arity = Arity(3)

    typeInferShort(arity)
      .shouldBe(
        """
          |Arity.^[ConstArity.Literal[Int(3)]]""".stripMargin
      )

    val dim = arity :<<- "abc"

    typeInferShort(dim)
      .shouldBe(
        """
          |ConstArity.Literal[Int(3)] :<<- String("abc")""".stripMargin
      )
  }
}
