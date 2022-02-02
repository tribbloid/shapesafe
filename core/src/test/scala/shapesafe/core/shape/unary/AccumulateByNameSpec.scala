package shapesafe.core.shape.unary

import shapesafe.BaseSpec
import shapesafe.core.Ops
import shapesafe.core.shape.{Names, Shape}

class AccumulateByNameSpec extends BaseSpec {

  it("treeString") {

    val s1 = Shape(2, 3) :<<=
      (Names >< "x" >< "y")

    val s2 = Shape(4, 5) :<<=
      (Names >< "x" >< "y")

    val rr = Ops.:+.reduceByName(s1, s2)

    rr.treeString.shouldBe(
      """
        |ArityOpsLike.:+ ‣ ArityOpsLike.Infix._ReduceByName ‣ AccumulateByName.On
        | ‣ OuterProduct
        |    ‣ GiveNames┏ 2:Literal ><
        |    :          ┃   3:Literal
        |    :          ┏ x ><
        |    :          ┃   y
        |    ‣ GiveNames┏ 4:Literal ><
        |               ┃   5:Literal
        |               ┏ x ><
        |               ┃   y
        |""".stripMargin
    )
  }

  describe("matrix") {

    val s1 =
      Shape(2, 3) :<<= (Names >< "x" >< "y")

    val s2 =
      Shape(4, 5) :<<= (Names >< "x" >< "y")

    it("direct sum") {

      val rr = Ops.:+.reduceByName(s1, s2)

      rr.eval.toString.shouldBe(
        """
          |6:Derived :<<- x ><
          |  8:Derived :<<- y
          |""".stripMargin
      )
    }

    it("Kronecker product") {

      val rr = Ops.:*.reduceByName(s1, s2)

      rr.eval.toString.shouldBe(
        """
            |8:Derived :<<- x ><
            |  15:Derived :<<- y
            |""".stripMargin
      )
    }
  }
}
