package shapesafe.core.shape.unary

import shapesafe.BaseSpec
import shapesafe.core.arity.Arity
import shapesafe.core.shape.{Names, Shape}

class RequireDistinctNamesSpec extends BaseSpec {

  describe("can eval if") {

    describe("singleton") {

      it("1") {

        val ss = Shape & Arity(1) :<<- "a"

        val rr = RequireDistinctNames(ss).^
        assert(ss.eval == rr.eval)
      }

      it("2") {

        val ss = Shape(1) :<<= (Names >< "a")

        val rr = RequireDistinctNames(ss).^
        assert(ss.eval == rr.eval)
      }
    }

    describe("shape has distinct names") {

      it("1") {

        val ss = Shape & Arity(1) :<<- "a" & Arity(2) :<<- "b"

        val rr = RequireDistinctNames(ss).^
        assert(ss.eval == rr.eval)
      }

      it("2") {

        val ss = Shape(1, 2) :<<= (Names >< "a" >< "b")

        val sse = ss.eval

        val rr = RequireDistinctNames(ss).^
        assert(ss.eval == rr.eval)
      }

      it("3") {

        val ss = Shape(1, 2, 3) :<<= (Names >< "a" >< "b" >< "c")

        val rr = RequireDistinctNames(ss).^
        assert(ss.eval == rr.eval)
      }
    }
  }

//  describe("shape has either no name or distinct names") {
//
//    it("1") {
//
//      val ss = Shape & Arity(1) & Arity(2)
//
//      val rr = RequireDistinctNames(ss).^
//      assert(ss.eval == rr.eval)
//    }
//
//    it("2") {
//
//      val ss = Shape & Arity(1) & Arity(2) & Arity(3) :<<- "c"
//
//      val rr = RequireDistinctNames(ss).^
//      assert(ss.eval == rr.eval)
//    }
//  }

  describe("CANNOT eval if") {
    it("shape has duplicated names") {

      val ss = Shape(1, 2, 3) :<<= (Names >< "a" >< "b" >< "a")

      val rr = RequireDistinctNames(ss).^

      shouldNotCompile(
        """rr.eval""",
        """.*(Names has duplicates).*"""
      )
    }
  }
}
