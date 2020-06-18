package edu.umontreal.kotlingrad.shapesafe.core.tensor

import edu.umontreal.kotlingrad.shapesafe.BaseSpec
import edu.umontreal.kotlingrad.shapesafe.common.arity.Arity
import shapeless.{HNil, ProductArgs, Witness}

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
    ) // TODO: scalafmt in IDEA failed here

    v.arity.internal.requireEqual(50)
  }

  it(s"... won't cause ${classOf[ProductArgs].getSimpleName}.applyDynamic to contaminate compile-time method validation") {

    shouldNotCompile(
      "DoubleVector.dummy(1.0)"
    )
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

  describe("zeros") {

    it("(literal)") {
      val v0 = DoubleVector.zeros(3)

      //    val v0type = ScalaReflection.universe.typeOf[v0.arity.type ].finalResultType
      //    println(v0type)

      v0.arity.internal.proveSame[Witness.`3`.T]
      v0.arity.internal.proveEqual[Witness.`3`.T]
      v0.arity.internal.requireEqual(3)

      //    v0.arity.internal.proveSame[Witness.`4`.T]
      //    v0.arity.internal.proveEqual[Witness.`4`.T]

      shouldNotCompile {
        "v0.arity.internal.proveSame[Witness.`4`.T]"
      }
      shouldNotCompile {
        "v0.arity.internal.proveEqual[Witness.`4`.T]"
      }
      shouldNotCompile(
        "v0.arity.internal.requireEqual(4)"
      )
    }

    it("(stable val)") {

      val v1 = DoubleVector.zeros(stable)
      //    v1.arity.internal.requireEqual(3) //TODO: Calculation has returned a non-literal type/value
    }

    it("(stable lazy val)") {

      val v2 = DoubleVector.zeros(stableLzy)
      //    v2.arity.internal.requireEqual(3) //TODO: Calculation has returned a non-literal type/value
    }

    it("unsupported") {
      shouldNotCompile(
        "DoubleVector.zeros(Random.nextInt(5))"
      )
      shouldNotCompile(
        "DoubleVector.zeros(value3)"
      )
      shouldNotCompile(
        "DoubleVector.zeros(value4)"
      )
    }
  }


  describe("dot_*") {

    it("type manually defined") {
      val w3 =Witness(3)
      type A3 = Arity.FromLiteral[w3.T]

      val w4 = Witness(4)
      type A4 = Arity.FromLiteral[w4.T]

      val v1: DoubleVector[A3] = DoubleVector.zeros(3)
      val v2: DoubleVector[A3] = DoubleVector.zeros(3)
      val v3: DoubleVector[A4]  = DoubleVector.zeros(4)

      assert((v1 dot_* v2) == 0.0)

      shouldNotCompile {
        "v1 dot_* v3"
      }
    }

    it("1") {

      val v0 = DoubleVector(1.0, 1.0, 1.0)
      val v1 = DoubleVector.zeros(3)
      val v2 = DoubleVector.zeros(3)
      val v3  = DoubleVector.zeros(4)

      assert((v0 dot_* v2) == 0.0)
      assert((v1 dot_* v2) == 0.0)
      assert((v1 dot_* v2) == 0.0)

      shouldNotCompile {
        "v1 dot_* v3"
      }
    }
  }

  describe("concat") {

    it("1") {

      val v0 = DoubleVector(1.0, 2.0)
      val v1 = DoubleVector.zeros(3)

      val result = v0 concat v1
      assert(result.data == Seq(1.0, 2.0, 0.0, 0.0, 0.0))
      result.arity.internal.requireEqual(5)

      val v2 = DoubleVector.zeros(5)
      val v3 = DoubleVector.zeros(6)

      assert((result dot_* v2) == 0.0)

      shouldNotCompile {
        "result dot_* v3"
      }
    }
  }

  describe("unsafe") {

    it("zeros") {

      val v = DoubleVector.unsafe.zeros(unstableFn)

      assert(v.arity.number == 3)
    }
  }

}
