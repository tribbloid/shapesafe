package org.shapesafe.core.shape.binary

import org.shapesafe.BaseSpec
import org.shapesafe.core.arity.{Arity, ArityAPI}
import org.shapesafe.core.shape.Shape

class OuterProductSpec extends BaseSpec {

  describe("outer") {

    it("1") {

      val s1 = Shape >|<
        Arity(2) :<<- "x"
      //        Arity.FromLiteral(3) :<<- "y"

      val s2 = Shape >|<
        Arity(2) :<<- "i"
      //        Arity.FromLiteral(3) :<<- "j"

      val rr = (s1 >< s2).eval

      typeInferShort(rr.shape).shouldBe(
        """
          |LeafShape.Eye >< (LeafArity.Literal[Int(2)] :<<- String("x")) >< (LeafArity.Literal[Int(2)] :<<- String("i"))""".stripMargin
      )
    }

    it("2") {

      val s1 = Shape >|<
        Arity(2) :<<- "x" >|<
        Arity(3) :<<- "y"

      val s2 = Shape >|<
        Arity(2) :<<- "i" >|<
        Arity(3) :<<- "j"

      val rr = (s1 outer s2).eval

      typeInferShort(rr.shape).shouldBe(
        """
          |LeafShape.Eye >< (LeafArity.Literal[Int(2)] :<<- String("x")) >< (LeafArity.Literal[Int(3)] :<<- String("y")) ><
          | (LeafArity.Literal[Int(2)] :<<- String("i")) >< (LeafArity.Literal[Int(3)] :<<- String("j"))
          |""".stripMargin.split('\n').mkString
      )
    }
  }
}
