package shapesafe.core.shape.binary

import shapesafe.BaseSpec
import shapesafe.core.Ops
import shapesafe.core.shape.Shape

abstract class Op2ByDimSpec extends BaseSpec {

  it("treeString") {
    val s1 = Shape(2, 3)
    val s2 = Shape(4, 5)

    val rr = s1.applyByDimDropLeft(Ops.:+, s2)

    rr.treeString.shouldBe(
      """
        |ArityOpsLike.:+ ‣ ArityOpsLike.Infix._Op2ByDim_DropLeft ‣ Op2ByDim.On┏ 2:Literal ><
        |                                                                     ┃   3:Literal
        |                                                                     ┏ 4:Literal ><
        |                                                                     ┃   5:Literal
        |""".stripMargin
    )
  }

  describe("matrix") {

    val s1 = Shape(2, 3)
    val s2 = Shape(4, 5)

    it("direct sum") {

      val strs = Seq(
        s1.applyByDim(Ops.:+, s2).eval.toString,
        s2.applyByDimDropLeft(Ops.:+, s2).eval.toString
      )

      strs
        .mkString("\n")
        .shouldBe(
          """
          |6:Derived ><
          |  8:Derived
          |""".stripMargin
        )
    }

    it("Kronecker product") {

      val strs = Seq(
        s1.applyByDim(Ops.:*, s2).eval.toString,
        s2.applyByDimDropLeft(Ops.:*, s2).eval.toString
      )
      strs
        .mkString("\n")
        .shouldBe(
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

  // TODO: should be a test in CanRefute or Refutes
//  describe(Ops.:+._Op2ByDim_Strict.IffRelay.getClass.getSimpleName) {
//
//    it("will FAIL if instance cannot be summoned") {
//
//      val s1 = Shape(2)
//      val s2 = Shape(3, 4)
//
//      type TT = Ops.:+._Op2ByDim_Strict.IffRelay[s1._ShapeType, s2._ShapeType]
//
//      shouldNotCompile(
//        """implicitly[TT]""",
//        """.*(\QDimension mismatch\E).*"""
//      )
//    }
//  }

  describe("applyByDim will FAIL if") {
    // TODO: is this really the best behaviour?

    it("operands has different dimensions") {

      val s1 = Shape(2, 3)
      val s2 = Shape(4, 5, 6)

      val rr = s1.applyByDim(Ops.:+, s2)

      shouldNotCompile(
        """rr.eval""",
        """.*(\QDimension mismatch\E).*"""
      )

    }

    it(" ... even if both operands are conjectures") {

      val s1 = Shape(2, 3).:<<=*("a", "b")
      val s2 = Shape(4, 5, 6).:<<=*("a", "b", "c")

      val rr = s1.applyByDim(Ops.:*, s2)

      shouldNotCompile(
        """rr.eval""",
        """.*(\QDimension mismatch\E).*"""
      )
    }
  }

  describe("applyByDimDropLeft can drop extra dimension(s) if") {
    // TODO: is this really the best behaviour?

    it("operands has different dimensions") {

      val s1 = Shape(2, 3)
      val s2 = Shape(4, 5, 6)

      val rr = s1.applyByDimDropLeft(Ops.:+, s2)

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

    it(" ... even if both operands are conjectures") {

      val s1 = Shape(2, 3).:<<=*("a", "b")
      val s2 = Shape(4, 5, 6).:<<=*("a", "b", "c")

      val rr = s1.applyByDimDropLeft(Ops.:*, s2)

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

  describe("shouldEqual") {}
}
