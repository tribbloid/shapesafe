package org.shapesafe.core.shape.unary

import org.shapesafe.BaseSpec
import org.shapesafe.core.shape.{Names, Shape}

class CheckDistinctSpec extends BaseSpec {

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
}
