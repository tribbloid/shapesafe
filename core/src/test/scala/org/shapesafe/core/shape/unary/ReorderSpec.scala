package org.shapesafe.core.shape.unary

import org.shapesafe.BaseSpec
import org.shapesafe.core.arity.Arity
import org.shapesafe.core.shape.Index.Name
import org.shapesafe.core.shape.{Indices, Names, Shape}

class ReorderSpec extends BaseSpec {

  val s1 = Shape >|<
    (Arity(1) :<<- "x") >|<
    Arity(2) :<<- "y" >|<
    Arity(3) :<<- "z"

  describe("Premise") {
    it("eye") {

      val ss = Reorder(s1, Indices.Eye)
      val rr = Reorder.Premise.apply(ss)
      typeInferShort(rr).shouldBe("LeafShape.Eye")
    }

    it("1") {

      val ss = Reorder(s1, Indices >< Name("z"))
      val rr = Reorder.Premise.apply(ss)
      typeInferShort(rr).shouldBe(
        """LeafShape.Eye >< (LeafArity.Literal[Int(3)] :<<- String("z"))"""
      )
    }

    it("2") {

      val ss = Reorder(s1, Indices >< Name("z") >< Name("y"))
      val rr = Reorder.Premise.apply(ss)
      typeInferShort(rr).shouldBe(
        """LeafShape.Eye >< (LeafArity.Literal[Int(3)] :<<- String("z")) >< (LeafArity.Literal[Int(2)] :<<- String("y"))"""
      )
    }

  }

  it("with names") {

    val r = Reorder(s1, Indices >< Name("z") >< Name("y"))

    r.eval.toString.shouldBe(
      """
        |Eye ><
        |  3:Literal :<<- z ><
        |  2:Literal :<<- y
        |""".stripMargin
    )
  }

  it(" ... alternatively") {

    val r = Reorder(s1, Names >< "z" >< "y")

    r.eval.toString.shouldBe(
      """
        |Eye ><
        |  3:Literal :<<- z ><
        |  2:Literal :<<- y
        |""".stripMargin
    )
  }
}