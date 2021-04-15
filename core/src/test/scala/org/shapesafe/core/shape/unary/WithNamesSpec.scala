package org.shapesafe.core.shape.unary

import com.tribbloids.graph.commons.util.viz.TypeViz
import org.shapesafe.BaseSpec
import org.shapesafe.core.arity.Arity
import org.shapesafe.core.shape.ShapeReporters.PeekShape
import org.shapesafe.core.shape.unary.Conjecture1.Refute1
import org.shapesafe.core.shape.{Names, Shape}

class WithNamesSpec extends BaseSpec {

  val shape = Shape >|<
    Arity(2) :<<- "x" >|<
    Arity(3) :<<- "y"

  val shapeSansName = Shape(2, 3)

  val ab = Names >< "a" >< "b"
  val ij = Names >< "i" >< "j"
  val namesTooMany = Names >< "a" >< "b" >< "c"

  val shapeRenamed = Shape >|<
    Arity(2) :<<- "a" >|<
    Arity(3) :<<- "b"

  describe("|<<-") {

    it("1") {

      //      inferShort(names1).shouldBe()

      val shape2 = (shape |<<- ab).eval

      //      VizType.infer(shape2).toString.shouldBe()

      TypeViz.infer(shape2).should_=:=(TypeViz.infer(shapeRenamed))
    }

    it("2") {

      val shape1 = shape |<<- ij

      val shape2 = (shape1 |<<- Names >< "a" >< "b").eval

      //      VizType.infer(shape2).toString.shouldBe()

      TypeViz.infer(shape2).should_=:=(TypeViz.infer(shapeRenamed))
    }

    it("3") {

      val shape1 = shape |<<- ij

      val shape2 = (shape1.named("a", "b")).eval

      TypeViz.infer(shape2).should_=:=(TypeViz.infer(shapeRenamed))

    }
  }

  describe("CANNOT eval if") {

    val good = shapeSansName |<<- ab
    val bad = shapeSansName |<<- namesTooMany

    describe("peek") {

      it("getReportMsg") {

        val msg = PeekShape
          .From[good._Shape]()
          .getReportMsg

        msg.toString.shouldBe(
          """
            ||>    [Eye] >< 2 >< 3 |<<- [Eye] >< a >< b
            |  :=  [Eye] >< (2 :<<- a) >< (3 :<<- b)
            |""".stripMargin
        )
      }

      it("1") {

        val s = good
        shouldNotCompile(
          """s.peek""",
          """.*(\Q  :=  [Eye] >< (2 :<<- a) >< (3 :<<- b)\E)"""
        )
      }

      it("2") {

        val s = bad
        shouldNotCompile(
          """s.peek""",
          """.*(\Q[Eye] >< 2 >< 3 |<<- [Eye] >< a >< b >< c\E)"""
        )
      }
    }

    describe("refute") {

      it("getReportMsg") {

        val msg = Refute1
          .From[good._Shape]()
          .getReportMsg

        msg.toString.shouldBe(
          """
            |¯\_(ツ)_/¯  ∄ [Eye] >< 2 >< 3 |<<- [Eye] >< a >< b
            |    -<< 1 condition >>-
            ||>    [Eye] >< 2 >< 3
            |""".stripMargin
        )
      }
    }

    it("operands has different dimensions") {

      val s = bad

      shouldNotCompile(
        "Conjecture1.refute1[s._Shape]",
        ".*(\\Q|>    [Eye] >< 2 >< 3\\E)"
      )

      shouldNotCompile(
        "s.eval",
        ".*(\\Q|>    [Eye] >< 2 >< 3\\E)"
      )
    }

    it(" ... even if S1 is a conjecture") {

      val s = shapeSansName |<<- ab |<<- namesTooMany

      shouldNotCompile(
        "s.eval",
        ".*(\\Q  :=  [Eye] >< (2 :<<- a) >< (3 :<<- b)\\E)"
      )
    }
  }

}
