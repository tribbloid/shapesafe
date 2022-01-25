package shapesafe.core.shape.unary

import shapesafe.BaseSpec
import shapesafe.core.arity.Arity
import shapesafe.core.shape.Index.Name
import shapesafe.core.shape.{Indices, Names, Shape}

class ReorderSpec extends BaseSpec {

  val s1 = Shape &
    (Arity(1) :<<- "x") &
    Arity(2) :<<- "y" &
    Arity(3) :<<- "z"

  describe("Premise") {
    it("eye") {

      val ss = Reorder(s1, Indices.Eye)
      val rr = Reorder.Premise.apply(ss)
      typeInferShort(rr).shouldBe("StaticShape.Eye")
    }

    it("1") {

      val ss = Reorder(s1, Indices >< Name("z"))
      val rr = Reorder.Premise.apply(ss)
      typeInferShort(rr).shouldBe(
        """StaticShape.Eye >< (ConstArity.Literal[Int(3)] :<<- String("z"))"""
      )
    }

    it("2") {

      val ss = Reorder(s1, Indices >< Name("z") >< Name("y"))
      val rr = Reorder.Premise.apply(ss)

      val s2 = Shape &
        Arity(3) :<<- "z" &
        Arity(2) :<<- "y"

      TypeViz.infer(s2.shape).should_=:=(TypeViz.infer(rr))

      typeInferShort(rr).shouldBe(
        """
          |StaticShape.Eye >< (ConstArity.Literal[Int(3)] :<<- String("z")) >< (ConstArity.Literal[Int(2)] :<<- String("y"))
          |""".stripMargin
      )
    }

    it("3") {

      val ss = Reorder(s1, Indices >< Name("z") >< Name("y") >< Name("x"))
      val rr = Reorder.Premise.apply(ss)
      typeInferShort(rr).shouldBe(
        """
          |StaticShape.Eye >< (ConstArity.Literal[Int(3)] :<<- String("z")) >< (ConstArity.Literal[Int(2)] :<<- String("y")) >< (ConstArity.Literal[Int(1)] :<<- String("x"))
          |""".stripMargin
      )
    }
  }

  it("with names") {

    val r = Reorder(s1, Indices >< Name("z") >< Name("y")).^

    r.eval.toString.shouldBe(
      """
        |3:Literal :<<- z ><
        |  2:Literal :<<- y
        |""".stripMargin
    )
  }

  it(" ... alternatively") {

    val r = Reorder(s1, Names >< "z" >< "y").^

    r.eval.toString.shouldBe(
      """
        |3:Literal :<<- z ><
        |  2:Literal :<<- y
        |""".stripMargin
    )
  }
}
