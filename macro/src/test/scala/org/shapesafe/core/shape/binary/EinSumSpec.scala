package org.shapesafe.core.shape.binary

import org.shapesafe.BaseSpec
import org.shapesafe.core.arity.Arity
import org.shapesafe.core.shape.{LeafShape, Names, Shape}

class EinSumSpec extends BaseSpec {

  describe("ops") {

    it("eye") {

      val s1 = LeafShape.Eye

      val ops = LeafShape.einSumOps(s1)
    }

    it("1") {

      val s1 = Shape ><
        (Arity(1) :<<- "x")

      val ops = LeafShape.einSumOps(s1)
    }

    it("2") {

      val s1 = Shape ><
        (Arity(1) :<<- "x") ><
        (Arity(2) :<<- "y") ><
        (Arity(1) :<<- "x")

      val ops = LeafShape.einSumOps(s1)
    }

  }

  describe("can einSum") {

    it("1") {

      val s1 = LeafShape.Eye

      val ops = s1.einSum(
        Shape ><
          (Arity(1) :<<- "i")
      )

      val r = ops --> (Names >< "i")

      r.toString.shouldBe(
        """
          |Eye ><
          |  1:Literal :<<- i
          |""".stripMargin
      )
    }

    it("2") {
      val s1 = Shape ><
        (Arity(1) :<<- "x") ><
        Arity(2) :<<- "y"

      val ops = s1.einSum(
        Shape ><
          (Arity(3) :<<- "i") ><
          Arity(4) :<<- "j"
      )

      val r = ops --> (Names >< "x" >< "i")

      r.toString.shouldBe(
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

      val r = (s1 einSum s2) --> ("x" >< "i")

      r.toString.shouldBe(
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

      shouldNotCompile(
        """s1 einSum s2"""
      )
    }

    it("2nd operand has dimension mismatch") {

      import Names.Syntax._

      val s1 = Shape(1, 2) |<<-
        ("x" >< "y")

      val s2 = Shape(3, 4) |<<-
        ("i" >< "i")

      shouldNotCompile(
        """s1 einSum s2"""
      )
    }

    it("result has dimension mismatch") {

      import Names.Syntax._

      val s1 = Shape(1, 2) |<<-
        ("x" >< "y")

      val s2 = Shape(3, 4) |<<-
        ("y" >< "z")

      shouldNotCompile(
        """s1 einSum s2"""
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
