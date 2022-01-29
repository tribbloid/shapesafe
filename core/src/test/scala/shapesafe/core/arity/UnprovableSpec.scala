package shapesafe.core.arity

import shapesafe.BaseSpec

class UnprovableSpec extends BaseSpec {

  it("Op2 with Unprovable should fail") {

    val v = Arity(3) :+ Arity.Unprovable

    shouldNotCompile(
      """v.eval""",
      """.*(Illegal Operation).*"""
    )
  }

  it("Require with Unprovable should fail") {

    val v = Arity(3) ==! Arity.Unprovable

    shouldNotCompile(
      """v.eval""",
      """.*(!=).*"""
    )
  }
}
