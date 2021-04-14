package org.shapesafe.core.shape.unary

import org.shapesafe.BaseSpec
import org.shapesafe.core.arity.Arity
import org.shapesafe.core.shape.{LeafShape, Names, Shape}
import shapeless.HNil

class EinSumSpec extends BaseSpec {

  import shapeless.syntax.singleton.mkSingletonOps

  it("treeString") {
    import Names.Syntax._

    val s1 =
      Shape(1, 2) |<<- ("x" >< "y")

    val s2 =
      Shape(3, 4) |<<- ("i" >< "j")

    val r = (s1 einSum s2) --> ("x" >< "i")

    r.treeString.shouldBe(
      """
        |Reorder┏ [Eye] ><
        |       ┃   x ><
        |       ┃   i
        | ‣ CheckEinSum
        |    ‣ OuterProduct
        |       ‣ CheckEinSum
        |       :  ‣ |<<-┏ [Eye] ><
        |       :        ┃   x ><
        |       :        ┃   y
        |       :     ‣ [Eye] ><
        |       :         1:Literal ><
        |       :         2:Literal
        |       ‣ CheckEinSum
        |          ‣ |<<-┏ [Eye] ><
        |                ┃   i ><
        |                ┃   j
        |             ‣ [Eye] ><
        |                 3:Literal ><
        |                 4:Literal
        |""".stripMargin
    )
  }

  describe("Indexing") {

    val indexing = CheckEinSum.indexing

    describe("can create") {

      it("singleton") {

        //      val eye = EinSumOperand.Eye

        val h1 = indexing(("i" ->> Arity(3).arity) :: HNil)
        h1.toString.shouldBe(
          """3:Literal :: HNil"""
        )
      }

      it("if names has no duplicate") {

        val h1 = indexing(
          ("i" ->> Arity(3).arity) ::
            ("j" ->> Arity(4).arity) ::
            HNil
        )

        val h2 = indexing(
          ("k" ->> Arity(5).arity) ::
            h1
        )

        h2.toString.shouldBe("""5:Literal :: 3:Literal :: 4:Literal :: HNil""")
      }

      it("if name --> dimension has no conflict") {

        val ss = ("i" ->> Arity(3).arity) ::
          ("j" ->> Arity(4).arity) ::
          HNil

        //      VizType[op.Static].toString.shouldBe()
        //      val v1 = EinSumCondition.apply(op.static --> ("j" ->> Arity(4)))
        //      VizType.infer(v1).toString.shouldBe()

        val h1 = indexing(("j" ->> Arity(4).arity) :: ss)
        h1.toString.shouldBe(
          """4:Literal :: 3:Literal :: 4:Literal :: HNil"""
        )

        val h2 = indexing(("i" ->> Arity(3).arity) :: ss)
        h2.toString.shouldBe(
          """3:Literal :: 3:Literal :: 4:Literal :: HNil"""
        )
      }
    }

    describe("CANNOT create") {

      it("if name --> dimension has conflict") {

        val ss = ("i" ->> Arity(3).arity) ::
          ("j" ->> Arity(4)) ::
          HNil

        val h1 = ("j" ->> Arity(3).arity) :: ss

        shouldNotCompile(
          """indexing(h1)"""
        )
      }
    }
  }

  describe("can einSum") {

    it("eye") {

      val s1 = LeafShape.Eye.^

      val op = s1.einSum.all.eval
    }

    it("1") {

      val s1 = Shape >|<
        (Arity(1) :<<- "x")

      val op = s1.einSum.all.eval
    }

    it("2") {

      val s1 = Shape >|<
        (Arity(1) :<<- "x") >|<
        (Arity(2) :<<- "y")

//      TypeVizCT[s1.Record].show

      val op = s1.einSum.all.eval
    }

    it("3") {

      val s1 = Shape >|<
        (Arity(1) :<<- "x") >|<
        (Arity(1) :<<- "x")

      val op = s1.einSum.all.eval
    }

    it("4") {

      val s1 = Shape >|<
        (Arity(1) :<<- "x") >|<
        (Arity(2) :<<- "y") >|<
        (Arity(1) :<<- "x")

      val op = s1.einSum.all.eval
    }
  }

  describe("can ->") {

    it("1") {

      val s1 = LeafShape.Eye.^

      val ops = s1.einSum(
        Shape >|<
          (Arity(1) :<<- "i")
      )

      val r = ops --> (Names >< "i")

      r.eval.toString.shouldBe(
        """
          |[Eye] ><
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

      val r = ops --> (Names >< "x" >< "i")

      r.eval.toString.shouldBe(
        """
          |[Eye] ><
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

      val r = (s1 einSum s2) --> ("x" >< "i")

      r.eval.toString.shouldBe(
        """
          |[Eye] ><
          |  1:Literal :<<- x ><
          |  3:Literal :<<- i
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
        """val r = ops ---> ("x" >< "k")"""
      )
    }
  }
}
