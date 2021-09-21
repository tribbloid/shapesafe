package org.shapesafe.core.shape

import org.shapesafe.BaseSpec
import org.shapesafe.core.shape.unary.RequireDistinct

class ShapeReportersSpec extends BaseSpec {

  describe("can report") {

    it("Static") {

      val i = Shape(3, 4)

      val m = ShapeReporters.PeekShape.ForTerm(i.shape).getMessage
      assert(m.trim == "3 >< 4")
    }

    it("Unchecked") {

      val i = Shape.Unchecked

      val m = ShapeReporters.PeekShape.ForTerm(i.shape).getMessage
      assert(m.trim == "_UNCHECKED_")
    }

    it("Conjecture1") {

      val i = RequireDistinct(Unchecked).^

      val m = ShapeReporters.PeekShape.ForTerm(i.shape).getMessage

      assert(
        m.trim ==
          """
            |_UNCHECKED_
            |
            |  :=  RequireDistinct[_UNCHECKED_]
            |""".stripMargin.trim
      )
    }

    it("Conjecture2") {

      val i = Shape.Unchecked >< Shape(3, 4)

      val m = ShapeReporters.PeekShape.ForTerm(i.shape).getMessage

//      import org.shapesafe.m.viz.PeekCT
//      PeekCT[i._Shape#Expr].interrupt

      assert(
        m.trim ==
          """
            |_UNCHECKED_
            |
            |  :=  _UNCHECKED_ >< (3 >< 4)
            |""".stripMargin.trim
      )
    }
  }
}
