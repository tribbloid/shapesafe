package shapesafe.core.shape

import shapesafe.BaseSpec
import shapesafe.core.shape.binary.OuterProduct
import shapesafe.core.shape.unary.RequireDistinct

class UncheckedSpec extends BaseSpec {

  describe("can be proven") {

    it("in Conjecture 1") {

      val conj = RequireDistinct(Unchecked)

      val evaled = conj.^.eval.shapeType

      assert(evaled == Unchecked)
    }

    it("in Conjecture 2") {

      val conj = OuterProduct(Unchecked, Shape(3, 4).shapeType)

      val evaled = conj.^.eval.shapeType

      assert(evaled == Unchecked)
    }
  }
}
