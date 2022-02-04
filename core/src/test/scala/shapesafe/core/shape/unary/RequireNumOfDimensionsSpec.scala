package shapesafe.core.shape.unary

import shapeless.Nat
import shapesafe.BaseSpec
import shapesafe.core.shape.Shape

class RequireNumOfDimensionsSpec extends BaseSpec {

  val v34 = Shape(3, 4)
  val i3j4 = v34.:<<=*("i", "j")

  describe("can eval if") {

//    v34.requireNumDim(Nat._2).interrupt

    it("nameless") {

      v34.requireNumDim(Nat._2).eval
    }

    it("named") {

      i3j4.requireNumDim(Nat._2).eval
    }
  }

  describe("cannot eval if") {
    it("nameless") {

      val rr = v34.requireNumDim(Nat._3)

      shouldNotCompile(
        """rr.eval""",
        """.*(Accepting only 3 dimension).*"""
      )
    }

    it("named") {

      val rr = i3j4.requireNumDim(Nat._1)

      shouldNotCompile(
        """rr.eval""",
        """.*(Accepting only 1 dimension).*"""
      )
    }
  }
}
