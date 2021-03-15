package org.shapesafe.core.shape.unary

import org.shapesafe.BaseSpec
import org.shapesafe.core.arity.Arity
import org.shapesafe.core.shape.{Index, Names, Shape}

class GetSpec extends BaseSpec {

  val s1 = Shape >|<
    (Arity(1) :<<- "x") >|<
    Arity(2) :<<- "y" >|<
    Arity(3) :<<- "z"

  it("by name") {

    val ss = Get(s1, Index.Name("x"))
//    VizType.infer(ee).shouldBe()
    val rr = ss.eval

    typeInferShort(rr).shouldBe(
      """LeafShape.Eye >< (LeafArity.Literal[Int(1)] :<<- String("x"))"""
    )
  }

  it(" ... indirectly") {

    val rr = Get(s1 |<<- Names >< "a" >< "b" >< "c", Index.Name("c")).eval

    typeInferShort(rr).shouldBe(
      """LeafShape.Eye >< (LeafArity.Literal[Int(3)] :<<- String("c"))"""
    )
  }

  it("by ordinal") {

    val ss = Get(s1, Index.I_th(0))
    val rr = ss.eval

    typeInferShort(rr).shouldBe(
      """LeafShape.Eye >< (LeafArity.Literal[Int(3)] :<<- String("z"))"""
    )
  }

  it(" .... indirectly") {

    val rr = Get(s1 |<<- Names >< "a" >< "b" >< "c", Index.I_th(1)).eval

    typeInferShort(rr).shouldBe(
      """LeafShape.Eye >< (LeafArity.Literal[Int(2)] :<<- String("b"))"""
    )
  }
}
