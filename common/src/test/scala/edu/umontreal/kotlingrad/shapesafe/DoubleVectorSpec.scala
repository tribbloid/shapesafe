package edu.umontreal.kotlingrad.shapesafe

import shapeless.HNil

class DoubleVectorSpec extends BaseSpec {

  it("apply") {

    val v = DoubleVector(1.0, 2.0, 3.0)

    v.arity.internal.requireEqual(3)
  }

  it("... with YUUGE number of args!") {

    val v = DoubleVector(
      1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 0.0, //
      1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 0.0, //
      1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 0.0, //
      1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 0.0, //
      1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 0.0, //
    ) // scalafmt in IDEA failed here

    v.arity.internal.requireEqual(50)
  }

  describe("from") {
    it("hList") {

      val v = DoubleVector.from.hList(1.0 :: 2.0 :: 3.0 :: HNil)

      v.arity.internal.requireEqual(3)
    }
  }

  val stable: Int = 1+2 // stable path!
  lazy val stableLzy: Int = 1+2 // stable path!

  def unstableFn: Int = 1+2 // no path
  var unstableVar: Int = 1+2 // no path

  it("zeros") {

    val v0 = DoubleVector.zeros(3)
    v0.arity.internal.requireEqual(3)

    val v1 = DoubleVector.zeros(stable)
//    v1.arity.internal.requireEqual(3) //TODO: Calculation has returned a non-literal type/value

    val v2 = DoubleVector.zeros(stableLzy)
//    v2.arity.internal.requireEqual(3) //TODO: Calculation has returned a non-literal type/value

    assertDoesNotCompile(
      """
        |DoubleVector.zeros(Random.nextInt(5))
        |""".stripMargin
    )

    assertDoesNotCompile(
      """
        |DoubleVector.zeros(value3)
        |""".stripMargin
    )

    assertDoesNotCompile(
      """
        |DoubleVector.zeros(value4)
        |""".stripMargin
    )
  }

  describe("unsafe") {

    it("zeros") {

      val v = DoubleVector.unsafe.zeros(unstableFn)

      assert(v.arity.number == 3)
      //      v.arity.proveEqual_internal[W.`3`.T]
    }
  }
}
