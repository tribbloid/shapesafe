package org.shapesafe.core.shape.unary

import org.shapesafe.BaseSpec
import org.shapesafe.core.arity.{Arity, ArityAPI}
import org.shapesafe.core.shape.{Index, Names, Shape}
import org.shapesafe.m.viz.TypeVizCT

class GetSubscriptSpec extends BaseSpec {

  val s1 = Shape >|<
    (Arity(1) :<<- "x") >|<
    Arity(2) :<<- "y" >|<
    Arity(3) :<<- "z"

  it("by name") {

    val ss = GetSubscript(s1, Index.Name("x")).^
//    VizType.infer(ee).shouldBe()
    val rr = ss.eval

    typeInferShort(rr.shape).shouldBe(
      """StaticShape.Eye >< (ConstArity.Literal[Int(1)] :<<- String("x"))"""
    )
  }

  it(" ... indirectly") {

    val ss = GetSubscript(s1 |<<- Names >< "a" >< "b" >< "c", Index.Name("c")).^
    val rr = ss.eval

    typeInferShort(rr.shape).shouldBe(
      """StaticShape.Eye >< (ConstArity.Literal[Int(3)] :<<- String("c"))"""
    )
  }

  it("by ordinal") {

    val ss = GetSubscript(s1, Index.I_th(0)).^
    val rr = ss.eval

    typeInferShort(rr.shape).shouldBe(
      """StaticShape.Eye >< (ConstArity.Literal[Int(3)] :<<- String("z"))"""
    )
  }

  it(" .... indirectly") {

    val ss = GetSubscript(s1 |<<- Names >< "a" >< "b" >< "c", Index.I_th(1))
    val rr = ss.^.eval

    typeInferShort(rr.shape).shouldBe(
      """StaticShape.Eye >< (ConstArity.Literal[Int(2)] :<<- String("b"))"""
    )
  }

  describe("Sub") {

    it("getByIndex") {

      val v = s1.Sub(0).eval.shape.head

      assert(v == Arity(3) :<<- "z") // HList is of inverse order
    }

    it("getByName") {

      val v = s1.Sub("y").eval.shape.head

      assert(v == Arity(2) :<<- "y")
    }
  }
}
