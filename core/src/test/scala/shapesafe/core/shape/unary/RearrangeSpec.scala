package shapesafe.core.shape.unary

import shapesafe.BaseSpec
import shapesafe.core.arity.Arity
import shapesafe.core.shape.Index.{LtoR, Name}
import shapesafe.core.shape.Names.tupleExtension
import shapesafe.core.shape.{Indices, Names, Shape}

class RearrangeSpec extends BaseSpec {

  val s1 = Shape &
    (Arity(1) :<<- "x") &
    Arity(2) :<<- "y" &
    Arity(3) :<<- "z"

  val s1Nameless = Shape &
    Arity(1) &
    Arity(2) &
    Arity(3)

  describe("Premise") {
    it("eye") {

      val ss = Rearrange(s1, Indices.Eye)
      val rr = Rearrange.Premise.apply(ss)
      typeInferShort(rr).shouldBe("StaticShape.Eye")
    }

    it("1") {

      val ss = Rearrange(s1, Indices >< Name("z"))
      val rr = Rearrange.Premise.apply(ss)
      typeInferShort(rr).shouldBe(
        """StaticShape.Eye >< (ConstArity.Literal[Int(3)] :<<- String("z"))"""
      )
    }

    it("2") {

      val ss = Rearrange(s1, Indices >< Name("z") >< Name("y"))
      val rr = Rearrange.Premise.apply(ss)

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

      val ss = Rearrange(s1, Indices >< Name("z") >< Name("y") >< Name("x"))
      val rr = Rearrange.Premise.apply(ss)
      typeInferShort(rr).shouldBe(
        """
          |StaticShape.Eye >< (ConstArity.Literal[Int(3)] :<<- String("z")) >< (ConstArity.Literal[Int(2)] :<<- String("y")) >< (ConstArity.Literal[Int(1)] :<<- String("x"))
          |""".stripMargin
      )
    }
  }

  describe("can rearrange") {

    it("with name") {

      val r = s1.rearrangeBy(Names >< "z")

      r.eval.toString.shouldBe(
        """
          |3:Literal :<<- z
          |""".stripMargin
      )
    }

    it("with names") {

      val strs = Seq(
        Rearrange(s1, Indices >< Name("z") >< Name("y")).^.eval.toString,
        Rearrange(s1, Names >< "z" >< "y").^.eval.toString,
        s1.rearrange("z", "y").eval.toString,
        s1.rearrangeBy(Names("z", "y")).eval.toString
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
        Rearrange(s1, Indices >< LtoR(2) >< LtoR(1)).^.eval.toString,
        s1.rearrangeBy(Indices >< LtoR(2) >< LtoR(1)).eval.toString
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

    it("even for nameless shapes") {

      val strs = Seq(
        Rearrange(s1Nameless, Indices >< LtoR(2) >< LtoR(1)).^.eval.toString,
        s1Nameless.rearrangeBy(Indices >< LtoR(2) >< LtoR(1)).eval.toString
      )

      strs.distinct
        .mkString("\n")
        .shouldBe(
          """
            |3:Literal ><
            |  2:Literal
            |""".stripMargin
        )
    }

    it("... alternative syntax") {

      import Names.Syntax._

      val shape = (Shape(1, 2, 3) :<<= ("a" >< "b" >< "c")).eval

      val r = shape.rearrangeBy("c" >< "b" >< "a")

      r.eval.toString.shouldBe(
        """
          |3:Literal :<<- c ><
          |  2:Literal :<<- b ><
          |  1:Literal :<<- a
          |""".stripMargin
      )
    }

    it("if shape doesn't have distinct names") {

      val shape = (Shape(1, 2, 3) :<<=* ("a", "b", "a")).eval

      val r = shape.rearrangeBy(Names("a", "b"))

      r.eval.toString.shouldBe(
        """
          |3:Literal :<<- a ><
          |  2:Literal :<<- b
          |""".stripMargin
      )
    }
  }

  describe("CANNOT rearrange if") {

    // TODO: no longer true
//    it("shape doesn't have distinct names") {
//
//      val shape = (Shape(1, 2, 3) :<<= ("a" >< "b" >< "a")).eval
//
//      val r = shape.selectBy(Names("a", "b"))
//
//      shouldNotCompile("""r.eval""")
//    }

    it("index with a given name cannot be found") {

      import Names.Syntax._

      val shape = (Shape(1, 2, 3) :<<= ("a" >< "b" >< "c")).eval

      val r = shape.rearrangeBy(Names("a", "i"))

      shouldNotCompile("""r.eval""")
    }
  }
}
