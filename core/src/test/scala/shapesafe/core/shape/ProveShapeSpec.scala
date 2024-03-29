package shapesafe.core.shape

import shapesafe.BaseSpec
import shapesafe.core.shape.ProveShape.AsLeafShape
import shapesafe.core.shape.unary.RequireDistinctNames

class ProveShapeSpec extends BaseSpec {

  describe("can report") {

    it("Static") {

      val i = Shape(3, 4)

      val m = AsLeafShape.Peek.ForTerm(i.shapeType).getMessage
      assert(m.trim == "3 >< 4")
    }

    it("Unchecked") {

      val i = Shape.Unchecked

      val m = AsLeafShape.Peek.ForTerm(i.shapeType).getMessage
      assert(m.trim == "_UNCHECKED_")
    }

    it("Conjecture1") {

      val i = RequireDistinctNames(Unchecked).^

      val m = AsLeafShape.Peek.ForTerm(i.shapeType).getMessage

      m shouldBe
        """
          |      _UNCHECKED_
          |  :=  RequireDistinctNames[_UNCHECKED_]
          |""".stripMargin
    }

    it("Conjecture2") {

      val i = Shape.Unchecked >< Shape(3, 4)

      val m = AsLeafShape.Peek.ForTerm(i.shapeType).getMessage

//      import shapesafe.m.viz.PeekCT
//      PeekCT[i._Shape#Expr].interrupt

      m shouldBe
        """
          |      _UNCHECKED_
          |  :=  _UNCHECKED_ >< (3 >< 4)
          |""".stripMargin
    }
  }
}
