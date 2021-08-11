package org.shapesafe.core.shape

import org.shapesafe.BaseSpec
import org.shapesafe.core.shape.binary.OuterProduct
import org.shapesafe.core.shape.unary.RequireDistinct

class UncheckedSpec extends BaseSpec {

  describe("can be proven") {

    it("in Conjecture 1") {

      val conj = RequireDistinct(Unchecked)

      val evaled = conj.^.eval.shape

      assert(evaled == Unchecked)
    }

    it("in Conjecture 2") {

      val conj = OuterProduct(Unchecked, Shape(3, 4).shape)

      val evaled = conj.^.eval.shape

      assert(evaled == Unchecked)
    }
  }
}
