package org.shapesafe.core.shape.unary

import org.shapesafe.BaseSpec
import org.shapesafe.core.arity.ops.ArityOps
import org.shapesafe.core.shape.{Names, Shape}

class OpByNameSpec extends BaseSpec {

  describe("matrix") {

    it("direct sum") {
      val s1 =
        Shape(2, 3) |<<- (Names >< "x" >< "y")

      val s2 =
        Shape(4, 5) |<<- (Names >< "x" >< "y")

      val rr = s1.reduceByName(ArityOps.:+, s2)

      //      rr.toString.shouldBe()

      rr.eval.toString.shouldBe(
        """
          |Eye ><
          |  6:Derived :<<- x ><
          |  8:Derived :<<- y
          |""".stripMargin
      )
    }

    it("Kronecker product") {

      val s1 =
        Shape(2, 3) |<<- (Names >< "x" >< "y")

      val s2 =
        Shape(4, 5) |<<- (Names >< "x" >< "y")

      val rr = s1.reduceByName(ArityOps.:*, s2)

      rr.eval.toString.shouldBe(
        """
            |Eye ><
            |  8:Derived :<<- x ><
            |  15:Derived :<<- y
            |""".stripMargin
      )
    }
  }
}
