package org.shapesafe.core.shape.binary

import org.shapesafe.BaseSpec
import org.shapesafe.core.arity.ops.ArityOps
import org.shapesafe.core.shape.Shape

class PairWiseSpec extends BaseSpec {

  it("treeString") {
    val s1 = Shape(2, 3)

    val s2 = Shape(4, 5)

    val rr = s1.zipWith(ArityOps.:+, s2)

    rr.treeString.shouldBe(
      """
        |ArityOpsLike.:+ ‣ ArityOpsLike.Infix._PairWise ‣ PairWise.On
        | ‣ [Eye] ><
        | :   2:Literal ><
        | :   3:Literal
        | ‣ [Eye] ><
        |     4:Literal ><
        |     5:Literal
        |""".stripMargin
    )
  }

  describe("matrix") {

    val s1 = Shape(2, 3)
    val s2 = Shape(4, 5)

    it("direct sum") {

      val rr = s1.zipWith(ArityOps.:+, s2)

//      TypeVizCT.infer(rr.shape).show

      rr.eval.toString.shouldBe(
        """
          |[Eye] ><
          |  6:Derived ><
          |  8:Derived
          |""".stripMargin
      )
    }

    it("Kronecker product") {

      val rr = s1.zipWith(ArityOps.:*, s2)

      rr.eval.toString.shouldBe(
        """
          |[Eye] ><
          |  8:Derived ><
          |  15:Derived
          |""".stripMargin
      )
    }

    it("shouldEqual") {

      s1.shouldEqual(s1).eval

      shouldNotCompile(
        """s1.shouldEqual(s2).eval"""
      )
    }
  }

  describe("can truncate extra dimension(s) if") {
    // TODO: is this really the best behaviour?

    it("operands has different dimensions") {

      val s1 = Shape(2, 3)
      val s2 = Shape(4, 5, 6)

      val rr = s1.zipWith(ArityOps.:+, s2)

      rr.eval.toString.shouldBe(
        """
          |[Eye] ><
          |  7:Derived ><
          |  9:Derived
          |""".stripMargin
      )

//      shouldNotCompile(
//        "rr.eval",
//        ".*(\\Q|>    [Eye] >< 2 >< 3\\E)"
//      )
    }

    it(" ... even if both operands are a conjectures") {

      val s1 = Shape(2, 3).|<<-*("a", "b")
      val s2 = Shape(4, 5, 6).|<<-*("a", "b", "c")

      val rr = s1.zipWith(ArityOps.:*, s2)

      rr.eval.toString.shouldBe(
        """
          |[Eye] ><
          |  10:Derived ><
          |  18:Derived
          |""".stripMargin
      )

//      shouldNotCompile(
//        "rr.eval",
//        ".*(\\Q|>    [Eye] >< 2 >< 3\\E)"
//      )
//
//      shouldNotCompile(
//        "s.eval",
//        ".*(\\Q  :=  [Eye] >< (2 :<<- a) >< (3 :<<- b)\\E)"
//      )
    }
  }
}
