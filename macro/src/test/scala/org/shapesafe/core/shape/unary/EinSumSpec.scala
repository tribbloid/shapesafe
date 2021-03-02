package org.shapesafe.core.shape.unary

import org.shapesafe.BaseSpec
import org.shapesafe.core.arity.Arity
import org.shapesafe.core.shape.{LeafShape, Names, Shape}
import shapeless.HNil

class EinSumSpec extends BaseSpec {

  import shapeless.syntax.singleton.mkSingletonOps

  describe("Indexing") {

    val index = CheckEinSum.indexing

    describe("can create") {

      it("singleton") {

        //      val eye = EinSumOperand.Eye

        val h1 = index(("i" ->> Arity(3)) :: HNil)
        h1.toString.shouldBe(
          """3:Literal :: HNil"""
        )
      }

      it("if names has no duplicate") {

        val h1 = index(
          ("i" ->> Arity(3)) ::
            ("j" ->> Arity(4)) ::
            HNil
        )

        val h2 = index(
          ("k" ->> Arity(5)) ::
            h1
        )

        h2.toString.shouldBe("""5:Literal :: 3:Literal :: 4:Literal :: HNil""")
      }

      it("if name -> dimension has no conflict") {

        val ss = ("i" ->> Arity(3)) ::
          ("j" ->> Arity(4)) ::
          HNil

        //      VizType[op.Static].toString.shouldBe()
        //      val v1 = EinSumCondition.apply(op.static -> ("j" ->> Arity(4)))
        //      VizType.infer(v1).toString.shouldBe()

        val h1 = index(("j" ->> Arity(4)) :: ss)
        h1.toString.shouldBe(
          """4:Literal :: 3:Literal :: 4:Literal :: HNil"""
        )

        val h2 = index(("i" ->> Arity(3)) :: ss)
        h2.toString.shouldBe(
          """3:Literal :: 3:Literal :: 4:Literal :: HNil"""
        )
      }
    }

    describe("CANNOT create") {

      it("if name -> dimension has conflict") {

        val ss = ("i" ->> Arity(3)) ::
          ("j" ->> Arity(4)) ::
          HNil

        val h1 = ("j" ->> Arity(3)) :: ss

        shouldNotCompile(
          """ii(h1)"""
        )
      }
    }
  }

  describe("can einSum") {

    it("eye") {

      val s1 = LeafShape.Eye

      val op = s1.einSum.eval
    }

    it("1") {

      val s1 = Shape >|<
        (Arity(1) :<<- "x")

      val op = s1.einSum.eval
    }

    it("2") {

      val s1 = Shape >|<
        (Arity(1) :<<- "x") >|<
        (Arity(2) :<<- "y") >|<
        (Arity(1) :<<- "x")

      val op = s1.einSum.eval
    }

  }

  describe("can ->") {

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

      val s1 =
        Shape(1, 2) |<<- ("x" >< "y")

      val s2 =
        Shape(3, 4) |<<- ("i" >< "j")

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

      val s1 =
        Shape(1, 2) |<<-
          ("x" >< "y")

      val s2 =
        Shape(3, 4) |<<-
          ("i" >< "j")

      val ops = s1 einSum s2

      shouldNotCompile(
        """val r = ops --> ("x" >< "k")"""
      )
    }
  }
}