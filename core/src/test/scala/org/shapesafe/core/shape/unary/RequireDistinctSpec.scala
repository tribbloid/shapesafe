package org.shapesafe.core.shape.unary

import org.shapesafe.BaseSpec
import org.shapesafe.core.arity.Arity
import org.shapesafe.core.shape.{Names, Shape}

class RequireDistinctSpec extends BaseSpec {

  import Names.Syntax._

  describe("can eval if") {

    describe("singleton") {

      it("1") {

        val ss = Shape >|< Arity(1) :<<- "a"

        val rr = RequireDistinct(ss).^
        assert(ss.eval == rr.eval)
      }

      it("2") {

        val ss = Shape(1) |<<- (Names >< "a")

        val rr = RequireDistinct(ss).^
        assert(ss.eval == rr.eval)
      }
    }

    describe("shape has distinct names") {

      it("1") {

        val ss = Shape >|< Arity(1) :<<- "a" >|< Arity(2) :<<- "b"

        val rr = RequireDistinct(ss).^
        assert(ss.eval == rr.eval)
      }

      it("2") {

        val ss = Shape(1, 2) |<<- (Names >< "a" >< "b")

        val sse = ss.eval

        val rr = RequireDistinct(ss).^
        assert(ss.eval == rr.eval)
      }

      it("3") {

        val ss = Shape(1, 2, 3) |<<- (Names >< "a" >< "b" >< "c")

        val rr = RequireDistinct(ss).^
        assert(ss.eval == rr.eval)
      }
    }
  }

  describe("CANNOT eval if") {
    it("shape has duplicated names") {

      val ss = Shape(1, 2, 3) |<<- (Names >< "a" >< "b" >< "a")

      val rr = RequireDistinct(ss).^

      shouldNotCompile(
        """rr.eval""",
        """.*(Names has duplicates).*"""
      )
    }
  }

  describe("can transpose") {

    it("1") {

      val shape = Shape >|<
        Arity(1) :<<- "a" >|<
        Arity(2) :<<- "b" >|<
        Arity(3) :<<- "c"

      val r = shape.transposeWith(Names >< "c")

      r.eval.toString.shouldBe(
        """
          |3:Literal :<<- c
          |""".stripMargin
      )
    }

    it("... alternative syntax") {

      val shape = (Shape(1, 2, 3) |<<- ("a" >< "b" >< "c")).eval

      val r = shape.transposeWith("c" >< "b" >< "a")

      r.eval.toString.shouldBe(
        """
          |3:Literal :<<- c ><
          |  2:Literal :<<- b ><
          |  1:Literal :<<- a
          |""".stripMargin
      )
    }
  }

  describe("CANNOT transpose if") {

    it("shape doesn't have distinct names") {

      val shape = (Shape(1, 2, 3) |<<- ("a" >< "b" >< "a")).eval

      val r = shape.transposeWith(Names("a", "b"))

      shouldNotCompile("""r.eval""")
    }

    it("index with a given name cannot be found") {

      val shape = (Shape(1, 2, 3) |<<- ("a" >< "b" >< "c")).eval

      val r = shape.transposeWith(Names("a", "i"))

      shouldNotCompile("""r.eval""")
    }
  }
}
