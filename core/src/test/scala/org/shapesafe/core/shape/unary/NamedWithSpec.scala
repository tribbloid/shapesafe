package org.shapesafe.core.shape.unary

import com.tribbloids.graph.commons.util.viz.TypeViz
import org.shapesafe.BaseSpec
import org.shapesafe.core.arity.Arity
import org.shapesafe.core.shape.ShapeReporters.PeekShape
import org.shapesafe.core.shape.{Names, Shape}

class NamedWithSpec extends BaseSpec {

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

    describe("peek & interrupt") {

      it("getReportMsg") {

        val msg = PeekShape
          .From[good._Shape]()
          .getReportMsg

        msg.toString.shouldBe(
          """
            |      ➊ >< (2 :<<- a) >< (3 :<<- b)
            |  :=  ➊ >< 2 >< 3 |<<- ➊ >< a >< b
            |""".stripMargin
        )
      }

      it("1") {

        val s = good
        shouldNotCompile(
          """s.interrupt""",
          """.*(\Q:=  ➊ >< 2 >< 3 |<<- ➊ >< a >< b\E).*"""
        )
      }

      it("2") {

        val s = bad
        shouldNotCompile(
          """s.interrupt""",
          """.*(\Q➊ >< 2 >< 3 |<<- ➊ >< a >< b >< c\E).*"""
        )
      }
    }

//    describe("refute") {
//
//      it("getReportMsg") {
//
//        val msg = Refute1
//          .From[good._Shape]()
//          .getReportMsg
//
//        msg.toString.shouldBe(
//          """
//            |Dimension mismatch
//            |➊ >< 2 >< 3 |<<- ➊ >< a >< b
//            |    -<< derived from 1 condition >>-
//            ||>    ➊ >< 2 >< 3
//            |""".stripMargin
//        )
//      }
//    }

    it("operands has different dimensions") {

      val s = bad

      shouldNotCompile(
        "s.eval",
        ".*(\\QDimension mismatch\\E).*"
      )
    }

    it(" ... even if S1 is a conjecture") {

      val s = shapeSansName |<<- ab |<<- namesTooMany

      shouldNotCompile(
        "s.eval",
        ".*(\\QDimension mismatch\\E).*"
      )
    }
  }

}
