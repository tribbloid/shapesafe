package shapesafe.core.shape.ops

import shapesafe.BaseSpec
import shapesafe.core.shape.Shape

class VectorOpsSuite extends BaseSpec {

  describe("dot") {
    it("works for vector3") {

      val s = Shape(3)
      val ss = s.dot(s).eval

      ss.toString.shouldBe(
        "1:Literal"
      )
    }

    it("works for vector4") {

      val s = Shape(4)
      val ss = s.dot(s).eval

      ss.toString.shouldBe(
        "1:Literal"
      )
    }
  }

  describe("cross") {

    it("works for vector3") {

      val s = Shape(3)
      val ss = s.cross(s).eval

      ss.toString.shouldBe(
        "3:Literal"
      )
    }

    it("won't work for vector3") {

      val s = Shape(4)
      val ss = s.cross(s)

      shouldNotCompile(
        "ss.eval",
        """.*(!=).*"""
      )
    }
  }
}
