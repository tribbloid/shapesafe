package org.shapesafe.core.shape.unary

import com.tribbloids.graph.commons.util.viz.VizType
import org.shapesafe.BaseSpec
import org.shapesafe.core.arity.LeafArity
import org.shapesafe.core.shape.{Names, Shape}

class WithNamesSpec extends BaseSpec {

  describe("withNames") {

    val shape = Shape >|<
      LeafArity.Literal(2) :<<- "x" >|<
      LeafArity.Literal(3) :<<- "y"

    val ab = Names >< "a" >< "b"
    val ij = Names >< "i" >< "j"
    val namesTooMany = Names >< "a" >< "b" >< "c"

    val shapeRenamed = Shape >|<
      LeafArity.Literal(2) :<<- "a" >|<
      LeafArity.Literal(3) :<<- "b"

    it("1") {

      //      inferShort(names1).shouldBe()

      val shape2 = (shape |<<- ab).simplify

      //      VizType.infer(shape2).toString.shouldBe()

      VizType.infer(shape2).shouldBe(VizType.infer(shapeRenamed))
    }

    it("2") {

      val shape1 = shape |<<- ij

      val shape2 = (shape1 |<<- Names >< "a" >< "b").simplify

      //      VizType.infer(shape2).toString.shouldBe()

      VizType.infer(shape2).shouldBe(VizType.infer(shapeRenamed))
    }

    it("3") {

      val shape1 = shape |<<- ij

      val shape2 = (shape1.named("a", "b")).simplify

      //      VizType.infer(shape2).toString.shouldBe()

      VizType.infer(shape2).shouldBe(VizType.infer(shapeRenamed))

    }

    it("should fail if operands are of different dimensions") {

      shouldNotCompile(
        "shape <<- namesTooMany"
      )
    }
  }
}
