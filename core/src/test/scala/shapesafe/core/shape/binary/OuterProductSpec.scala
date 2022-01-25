package shapesafe.core.shape.binary

import shapesafe.BaseSpec
import shapesafe.core.arity.Arity
import shapesafe.core.shape.Shape

class OuterProductSpec extends BaseSpec {

  describe("outer") {

    it("1") {

      val s1 = Shape &
        Arity(2) :<<- "x"
      //        Arity.FromLiteral(3) :<<- "y"

      val s2 = Shape &
        Arity(2) :<<- "i"
      //        Arity.FromLiteral(3) :<<- "j"

      val rr = (s1 >< s2).eval

      typeInferShort(rr.shape).shouldBe(
        """
          |StaticShape.Eye >< (ConstArity.Literal[Int(2)] :<<- String("x")) >< (ConstArity.Literal[Int(2)] :<<- String("i"))""".stripMargin
      )
    }

    it("2") {

      val s1 = Shape &
        Arity(2) :<<- "x" &
        Arity(3) :<<- "y"

      val s2 = Shape &
        Arity(2) :<<- "i" &
        Arity(3) :<<- "j"

      val rr = (s1 outer s2).eval

      typeInferShort(rr.shape).shouldBe(
        """
          |StaticShape.Eye >< (ConstArity.Literal[Int(2)] :<<- String("x")) >< (ConstArity.Literal[Int(3)] :<<- String("y")) ><
          | (ConstArity.Literal[Int(2)] :<<- String("i")) >< (ConstArity.Literal[Int(3)] :<<- String("j"))
          |""".stripMargin.split('\n').mkString
      )
    }
  }
}
