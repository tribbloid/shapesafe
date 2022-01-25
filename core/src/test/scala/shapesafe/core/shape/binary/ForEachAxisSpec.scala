package shapesafe.core.shape.binary

import shapesafe.BaseSpec
import shapesafe.core.Ops
import shapesafe.core.shape.Shape

class ForEachAxisSpec extends BaseSpec {

  it("treeString") {
    val s1 = Shape(2, 3)

    val s2 = Shape(4, 5)

    val rr = s1.foreachAxis(Ops.:+, s2)

    rr.treeString.shouldBe(
      """
        |ArityOpsLike.:+ ‣ ArityOpsLike.Infix._ForEachAxis ‣ ForEachAxis.On┏ 2:Literal ><
        |                                                                  ┃   3:Literal
        |                                                                  ┏ 4:Literal ><
        |                                                                  ┃   5:Literal
        |""".stripMargin
    )
  }

  describe("matrix") {

    val s1 = Shape(2, 3)
    val s2 = Shape(4, 5)

    it("direct sum") {

      val rr = s1.foreachAxis(Ops.:+, s2)

//      TypeVizCT.infer(rr.shape).show

      rr.eval.toString.shouldBe(
        """
          |6:Derived ><
          |  8:Derived
          |""".stripMargin
      )
    }

    it("Kronecker product") {

      val rr = s1.foreachAxis(Ops.:*, s2)

      rr.eval.toString.shouldBe(
        """
          |8:Derived ><
          |  15:Derived
          |""".stripMargin
      )
    }

    it("shouldEqual") {

      s1.requireEqual(s1).eval

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

      val rr = s1.foreachAxis(Ops.:+, s2)

      rr.eval.toString.shouldBe(
        """
          |7:Derived ><
          |  9:Derived
          |""".stripMargin
      )

//      shouldNotCompile(
//        "rr.eval",
//        ".*(\\Q|>    ∅ >< 2 >< 3\\E)"
//      )
    }

    it(" ... even if both operands are a conjectures") {

      val s1 = Shape(2, 3).:<<=*("a", "b")
      val s2 = Shape(4, 5, 6).:<<=*("a", "b", "c")

      val rr = s1.foreachAxis(Ops.:*, s2)

      rr.eval.toString.shouldBe(
        """
          |10:Derived ><
          |  18:Derived
          |""".stripMargin
      )

//      shouldNotCompile(
//        "rr.eval",
//        ".*(\\Q|>    ∅ >< 2 >< 3\\E)"
//      )
//
//      shouldNotCompile(
//        "s.eval",
//        ".*(\\Q  :=  ∅ >< (2 :<<- a) >< (3 :<<- b)\\E)"
//      )
    }
  }
}
