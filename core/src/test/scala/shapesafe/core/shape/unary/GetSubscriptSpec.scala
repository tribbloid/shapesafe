package shapesafe.core.shape.unary

import shapesafe.BaseSpec
import shapesafe.core.arity.Arity
import shapesafe.core.shape.{Index, Names, Shape}

class GetSubscriptSpec extends BaseSpec {

  val s1 = Shape &
    (Arity(1) :<<- "x") &
    Arity(2) :<<- "y" &
    Arity(3) :<<- "z"

  it("by name") {

    val ss = GetSubscript(s1, Index.Name("x")).^
//    VizType.infer(ee).shouldBe()
    val rr = ss.eval

    typeInferShort(rr.shapeType).shouldBe(
      """StaticShape.Eye >< (ConstArity.Literal[Int(1)] :<<- String("x"))"""
    )
  }

  it(" ... indirectly") {

    val ss = GetSubscript(s1 :<<= Names >< "a" >< "b" >< "c", Index.Name("c")).^
    val rr = ss.eval

    typeInferShort(rr.shapeType).shouldBe(
      """StaticShape.Eye >< (ConstArity.Literal[Int(3)] :<<- String("c"))"""
    )
  }

  it("by ordinal") {

    val ss = GetSubscript(s1, Index.I_th(0)).^
    val rr = ss.eval

    typeInferShort(rr.shapeType).shouldBe(
      """StaticShape.Eye >< (ConstArity.Literal[Int(3)] :<<- String("z"))"""
    )
  }

  it(" .... indirectly") {

    val ss = GetSubscript(s1 :<<= Names >< "a" >< "b" >< "c", Index.I_th(1))
    val rr = ss.^.eval

    typeInferShort(rr.shapeType).shouldBe(
      """StaticShape.Eye >< (ConstArity.Literal[Int(2)] :<<- String("b"))"""
    )
  }

  describe("Sub") {

    it("getByIndex") {

      val v = s1.select1(0).eval.shape.head

      assert(v == Arity(3) :<<- "z") // HList is of inverse order
    }

    it("getByName") {

      val v = s1.select1("y").eval.shape.head

      assert(v == Arity(2) :<<- "y")
    }
  }
}
