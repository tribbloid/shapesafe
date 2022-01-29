package shapesafe.core.shape.unary

import shapesafe.BaseSpec
import shapesafe.core.arity.Arity
import shapesafe.core.shape.Index.{Left, Name}
import shapesafe.core.shape.Names.tupleExtension
import shapesafe.core.shape.{Indices, Names, Shape}

class SelectSpec extends BaseSpec {

  val s1 = Shape &
    (Arity(1) :<<- "x") &
    Arity(2) :<<- "y" &
    Arity(3) :<<- "z"

  describe("Premise") {
    it("eye") {

      val ss = Select(s1, Indices.Eye)
      val rr = Select.Premise.apply(ss)
      typeInferShort(rr).shouldBe("StaticShape.Eye")
    }

    it("1") {

      val ss = Select(s1, Indices >< Name("z"))
      val rr = Select.Premise.apply(ss)
      typeInferShort(rr).shouldBe(
        """StaticShape.Eye >< (ConstArity.Literal[Int(3)] :<<- String("z"))"""
      )
    }

    it("2") {

      val ss = Select(s1, Indices >< Name("z") >< Name("y"))
      val rr = Select.Premise.apply(ss)

      val s2 = Shape &
        Arity(3) :<<- "z" &
        Arity(2) :<<- "y"

      TypeViz.infer(s2.shapeType).should_=:=(TypeViz.infer(rr))

      typeInferShort(rr).shouldBe(
        """
          |StaticShape.Eye >< (ConstArity.Literal[Int(3)] :<<- String("z")) >< (ConstArity.Literal[Int(2)] :<<- String("y"))
          |""".stripMargin
      )
    }

    it("3") {

      val ss = Select(s1, Indices >< Name("z") >< Name("y") >< Name("x"))
      val rr = Select.Premise.apply(ss)
      typeInferShort(rr).shouldBe(
        """
          |StaticShape.Eye >< (ConstArity.Literal[Int(3)] :<<- String("z")) >< (ConstArity.Literal[Int(2)] :<<- String("y")) >< (ConstArity.Literal[Int(1)] :<<- String("x"))
          |""".stripMargin
      )
    }
  }

  it("with names") {

    val strs = Seq(
      Select(s1, Indices >< Name("z") >< Name("y")).^.eval.toString,
      Select(s1, Names >< "z" >< "y").^.eval.toString,
      s1.select("z", "y").eval.toString,
      s1.selectBy(Names("z", "y")).eval.toString
    )

    strs.distinct
      .mkString("\n")
      .shouldBe(
        """
        |3:Literal :<<- z ><
        |  2:Literal :<<- y
        |""".stripMargin
      )
  }

  it("with indices") {
    val strs = Seq(
      Select(s1, Indices >< Left(2) >< Left(1)).^.eval.toString,
      s1.selectBy(Indices >< Left(2) >< Left(1)).eval.toString
    )

    strs.distinct
      .mkString("\n")
      .shouldBe(
        """
          |3:Literal :<<- z ><
          |  2:Literal :<<- y
          |""".stripMargin
      )
  }
}
