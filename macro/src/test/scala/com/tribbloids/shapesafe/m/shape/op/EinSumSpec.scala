package com.tribbloids.shapesafe.m.shape.op

import com.tribbloids.shapesafe.BaseSpec
import com.tribbloids.shapesafe.m.arity.Arity
import com.tribbloids.shapesafe.m.shape.{Names, Shape}

class EinSumSpec extends BaseSpec {

  describe("ops") {

    it("eye") {

      val s1 = Shape.Eye

      val ops = Shape.einSumOps(s1)
    }

    it("1") {

      val s1 = Shape ><
        (Arity(1) :<<- "x")

      val ops = Shape.einSumOps(s1)
    }

    it("2") {

      val s1 = Shape ><
        (Arity(1) :<<- "x") ><
        (Arity(2) :<<- "y") ><
        (Arity(1) :<<- "x")

      val ops = Shape.einSumOps(s1)
    }

  }

  describe("can einSum") {

    it("1") {

      val s1 = Shape.Eye

      val ops = s1.einSum(
        Shape ><
          (Arity(1) :<<- "i")
      )

      val r = ops --> (Names >< "i")

      r.toString.shouldBe(
        "Eye >< 1:Literal :<<- i"
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
        "Eye >< 1:Literal :<<- x >< 3:Literal :<<- i"
      )
    }

    it("2 - alternative syntax") {

      import Names.Syntax._

      val s1 = Shape(1, 2) |<<-
        ("x" >< "y")

      val s2 = Shape(3, 4) |<<-
        ("i" >< "j")

      val ops = s1 einSum s2

      val r = ops --> ("x" >< "i")

      r.toString.shouldBe(
        "Eye >< 1:Derived :<<- x >< 3:Derived :<<- i"
      )
    }
  }

  describe("cannot einsum if") {

    it("1st operand has dimension mismatch") {

      import Names.Syntax._

      val s1 = Shape(1, 2, 3) |<<-
        ("x" >< "y" >< "y")

      val s2 = Shape(3, 4) |<<-
        ("i" >< "j")

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
  }
}
