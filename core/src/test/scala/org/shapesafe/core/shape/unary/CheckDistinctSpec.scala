package org.shapesafe.core.shape.unary

import org.shapesafe.BaseSpec
import org.shapesafe.core.arity.Arity
import org.shapesafe.core.shape.{Names, Shape}

class CheckDistinctSpec extends BaseSpec {

  import Names.Syntax._

  describe("can verify if") {

    it("shape has distinct names") {

      val ss = Shape(1, 2, 3) |<<- (Names >< "a" >< "b" >< "c")

      val rr = CheckDistinct(ss)

      assert(ss.eval == rr.eval)
    }
  }

  describe("CANNOT verify if") {
    it("shape has duplicated names") {

      val ss = Shape(1, 2, 3) |<<- (Names >< "a" >< "b" >< "a")

      val rr = CheckDistinct(ss)

      shouldNotCompile("""rr.eval""")
    }
  }

  describe("can transpose") {

    it("1") {

      val shape = Shape >|<
        Arity(1) :<<- "a" >|<
        Arity(2) :<<- "b" >|<
        Arity(3) :<<- "c"

      val r = shape.transpose(Names >< "c")

      r.eval.toString.shouldBe(
        """
          |Eye ><
          |  3:Literal :<<- c
          |""".stripMargin
      )
    }

    it("... alternative syntax") {

      val shape = (Shape(1, 2, 3) |<<- ("a" >< "b" >< "c")).eval

      val r = shape.transpose("c" >< "b" >< "a")

      r.eval.toString.shouldBe(
        """
          |Eye ><
          |  3:Literal :<<- c ><
          |  2:Literal :<<- b ><
          |  1:Literal :<<- a
          |""".stripMargin
      )
    }
  }

  describe("CANNOT transpose if") {

    it("shape doesn't have distinct names") {

      val shape = (Shape(1, 2, 3) |<<- ("a" >< "b" >< "a")).eval

      val r = shape.transpose(Names("a", "b"))

      shouldNotCompile("""r.eval""")
    }

    it("index with a given name cannot be found") {

      val shape = (Shape(1, 2, 3) |<<- ("a" >< "b" >< "c")).eval

      val r = shape.transpose(Names("a", "i"))

      shouldNotCompile("""r.eval""")
    }
  }
}
