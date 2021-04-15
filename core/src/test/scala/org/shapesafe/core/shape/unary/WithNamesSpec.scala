package org.shapesafe.core.shape.unary

import com.tribbloids.graph.commons.util.viz.TypeViz
import org.shapesafe.BaseSpec
import org.shapesafe.core.arity.Arity
import org.shapesafe.core.shape.{Names, Shape}

class WithNamesSpec extends BaseSpec {

  describe("withNames") {

    val shape = Shape >|<
      Arity(2) :<<- "x" >|<
      Arity(3) :<<- "y"

    val ab = Names >< "a" >< "b"
    val ij = Names >< "i" >< "j"
    val namesTooMany = Names >< "a" >< "b" >< "c"

    val shapeRenamed = Shape >|<
      Arity(2) :<<- "a" >|<
      Arity(3) :<<- "b"

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

    it("should fail if operands are of different dimensions") {
//      (shape |<<- namesTooMany).eval

      shouldNotCompile(
        "(shape |<<- namesTooMany).eval"
      )
    }
  }

  describe("peek") {

    it("1") {

      val s = Shape(1, 2).|<<-*("a", "b")
      shouldNotCompile(
        """s.peek""",
        """.*(\Q[Eye] >< 1 >< 2 |<<- [Eye] >< a >< b := [Eye] >< (1 :<<- a) >< (2 :<<- b)\E)"""
      )
    }

    it("2") {

      val s = Shape(1, 2).|<<-*("a", "b", "c")
      shouldNotCompile(
        """s.peek""",
        """.*(\Q[Eye] >< 1 >< 2 |<<- [Eye] >< a >< b >< c\E)"""
      )
    }
  }
}
