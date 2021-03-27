package org.shapesafe.core.shape.unary

import org.shapesafe.BaseSpec
import org.shapesafe.core.arity.ops.ArityOps
import org.shapesafe.core.shape.{Names, Shape}

class ReduceByNameSpec extends BaseSpec {

  it("treeString") {

    val s1 =
      Shape(2, 3) |<<- (Names >< "x" >< "y")

    val s2 =
      Shape(4, 5) |<<- (Names >< "x" >< "y")

    val rr = s1.flattenWith(ArityOps.:+, s2)

    rr.treeString.shouldBe(
      """
        |ArityOpsLike.:+ ‣ ArityOpsLike.Infix.SquashByName ‣ ReduceByName.On
        | ‣ OuterProduct
        |    ‣ |<<-┏ Eye ><
        |    :     ┃   x ><
        |    :     ┃   y
        |    :  ‣ Eye ><
        |    :      2:Literal ><
        |    :      3:Literal
        |    ‣ |<<-┏ Eye ><
        |          ┃   x ><
        |          ┃   y
        |       ‣ Eye ><
        |           4:Literal ><
        |           5:Literal
        |""".stripMargin
    )
  }

  describe("matrix") {

    val s1 =
      Shape(2, 3) |<<- (Names >< "x" >< "y")

    val s2 =
      Shape(4, 5) |<<- (Names >< "x" >< "y")

    it("direct sum") {

      val rr = s1.flattenWith(ArityOps.:+, s2)

      rr.eval.toString.shouldBe(
        """
          |Eye ><
          |  6:Derived :<<- x ><
          |  8:Derived :<<- y
          |""".stripMargin
      )
    }

    it("Kronecker product") {

      val rr = s1.flattenWith(ArityOps.:*, s2)

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
