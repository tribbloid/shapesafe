package org.shapesafe.core.shape.unary

import org.shapesafe.BaseSpec
import org.shapesafe.core.arity.Arity
import org.shapesafe.core.shape.{LeafShape, Names, Shape}

class EinSumSpec extends BaseSpec {

  describe("ops") {

    it("eye") {

      val s1 = LeafShape.Eye

      val ops = s1.einSum.eval
    }

    it("1") {

      val s1 = Shape >|<
        (Arity(1) :<<- "x")

      val ops = s1.einSum.eval
    }

    it("2") {

      val s1 = Shape >|<
        (Arity(1) :<<- "x") >|<
        (Arity(2) :<<- "y") >|<
        (Arity(1) :<<- "x")

      val ops = s1.einSum.eval
    }

  }

  describe("can einSum") {

    it("1") {

      val s1 = LeafShape.Eye

      val ops = s1.einSum(
        Shape >|<
          (Arity(1) :<<- "i")
      )

      val r = ops -> (Names >< "i")

      r.eval.toString.shouldBe(
        """
          |Eye ><
          |  1:Literal :<<- i
          |""".stripMargin
      )
    }

    it("2") {
      val s1 = Shape >|<
        (Arity(1) :<<- "x") >|<
        Arity(2) :<<- "y"

      val ops = s1.einSum(
        Shape >|<
          (Arity(3) :<<- "i") >|<
          Arity(4) :<<- "j"
      )

      val r = ops -> (Names >< "x" >< "i")

      r.eval.toString.shouldBe(
        """
          |Eye ><
          |  1:Literal :<<- x ><
          |  3:Literal :<<- i
          |""".stripMargin
      )
    }

    it("... alternative syntax") {

      import Names.Syntax._

      val s1 = (
        Shape(1, 2) |<<- ("x" >< "y")
      ).eval

      val s2 = (
        Shape(3, 4) |<<- ("i" >< "j")
      ).eval

      val r = (s1 einSum s2) -> ("x" >< "i")

      r.eval.toString.shouldBe(
        """
          |Eye ><
          |  1:Derived :<<- x ><
          |  3:Derived :<<- i
          |""".stripMargin
      )
    }
  }

  describe("cannot einsum if") {

    it("1st operand has dimension mismatch") {

      import Names.Syntax._

      val s1 = Shape(1, 2, 3) |<<- ("x" >< "y" >< "y")
      val s2 = Shape(3, 4) |<<- ("i" >< "j")
      val r = s1 einSum s2

      shouldNotCompile(
        """r.eval"""
      )
    }

    it("2nd operand has dimension mismatch") {

      import Names.Syntax._

      val s1 = Shape(1, 2) |<<-
        ("x" >< "y")
      val s2 = Shape(3, 4) |<<-
        ("i" >< "i")
      val r = s1 einSum s2

      shouldNotCompile(
        """r.eval"""
      )
    }

    it("result has dimension mismatch") {

      import Names.Syntax._

      val s1 = Shape(1, 2) |<<-
        ("x" >< "y")
      val s2 = Shape(3, 4) |<<-
        ("y" >< "z")
      val r = s1 einSum s2

      shouldNotCompile(
        """r.eval"""
      )
    }

    it("result refers to non-existing index") {

      import Names.Syntax._

      val s1 = (
        Shape(1, 2) |<<-
          ("x" >< "y")
      ).eval

      val s2 = (
        Shape(3, 4) |<<-
          ("i" >< "j")
      ).eval

      val ops = s1 einSum s2

      shouldNotCompile(
        """val r = ops --> ("x" >< "k")"""
      )
    }
  }
}
