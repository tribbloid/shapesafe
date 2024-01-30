package shapesafe.verify.breeze.tensor

import breeze.linalg.DenseVector
import shapesafe.BaseSpec
import shapeless.{HNil, ProductArgs, Witness}

class DoubleVectorSpec extends BaseSpec {

  it("apply") {

    val v = DoubleVector(1.0, 2.0, 3.0)

    v.arity.proveEqual(3)
  }

  it("... with YUUGE number of args!") {

    val v = DoubleVector(
      1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 0.0, //
      1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 0.0, //
      1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 0.0, //
      1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 0.0, //
      1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 0.0 //
    )

    v.arity.proveEqual(50)
  }

  it(
    s"... won't cause ${classOf[ProductArgs].getSimpleName}.applyDynamic to contaminate compile-time method validation"
  ) {

    shouldNotCompile(
      "DoubleVector.dummy(1.0)"
    )
  }

  describe("from") {
    it("hList") {

      val v = DoubleVector.from.hList(1.0 :: 2.0 :: 3.0 :: HNil)

      v.arity.proveEqual(3)
    }
  }

  val stable: Int = 1 + 2 // stable path!
  lazy val stableLzy: Int = 1 + 2 // stable path!

  def unstableFn: Int = 1 + 2 // no path
  var unstableVar: Int = 1 + 2 // no path

  describe("zeros") {

    it("(literal)") {
      val v0 = DoubleVector.zeros(3)

      v0.arity.proveSameType[Witness.`3`.T]
      v0.arity.proveEqualType[Witness.`3`.T]
      v0.arity.proveEqual(3)

      //    v0.arity.internal.proveSame[Witness.`4`.T]
      //    v0.arity.internal.proveEqual[Witness.`4`.T]

      shouldNotCompile {
        "v0.arity.internal.proveSame[Witness.`4`.T]"
      }
      shouldNotCompile {
        "v0.arity.internal.proveEqual[Witness.`4`.T]"
      }
      shouldNotCompile(
        "v0.arity.internal.proveEqual(4)"
      )
    }

    it("(stable val)") {

      DoubleVector.zeros(stable)
      //    v1.arity.internal.proveEqual(3) //TODO: Calculation has returned a non-literal type/value
    }

    it("(stable lazy val)") {

      DoubleVector.zeros(stableLzy)
      //    v2.arity.internal.proveEqual(3) //TODO: Calculation has returned a non-literal type/value
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
      val w3 = Witness(3)

      val w4 = Witness(4)

      val v1 = DoubleVector.zeros(3)
      val v2 = DoubleVector.zeros(3)
      val v3 = DoubleVector.zeros(4)

      assert((v1 dot_* v2) == 0.0)

      shouldNotCompile {
        "v1 dot_* v3"
      }
    }

    it("1") {

      val v0 = DoubleVector(1.0, 1.0, 1.0)
      val v1 = DoubleVector.zeros(3)
      val v2 = DoubleVector.zeros(3)
      val v3 = DoubleVector.zeros(4)

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
      assert(result.data == DenseVector(1.0, 2.0, 0.0, 0.0, 0.0))
      result.arity.proveEqual(5)

      val v2 = DoubleVector.zeros(5)
      val v3 = DoubleVector.zeros(6)

      assert((result dot_* v2) == 0.0)

      shouldNotCompile {
        "result dot_* v3"
      }
    }
  }

  describe("Reified") {

    val v0 = DoubleVector.random(6)

    it("arity") {

      val aa = v0.arity

      aa.proveSameType[Witness.`6`.T]
      aa.proveEqualType[Witness.`6`.T]
      aa.proveEqual(6)

//      aa.internal._can_+(3)
    }

    it("crossValidate") {

      v0.crossValidate()
    }
  }

  describe("pad") {

    it("1") {

      val v0 = DoubleVector.random(6)

      val result = v0.pad(2)
      result.crossValidate()
      result.arity.proveEqual(10)
    }
  }

  describe("conv") {

    it("1") {

      val v0 = DoubleVector.random(6)
      val v1 = DoubleVector.random(3)

      val result = v0.conv(v1)
      result.crossValidate()
      result.arity.proveEqual(4)
    }

    it("2") {

      val v0 = DoubleVector.random(6)
      val v1 = DoubleVector.random(3)
      val v2 = DoubleVector.random(2)

      {
        val result = v0.conv(v1, 2)
        result.crossValidate()
        result.arity.proveEqual(2)
      }

      {
        val result = v0.conv(v2, 2)
        result.crossValidate()
        result.arity.proveEqual(2)
      }
    }
  }

//  describe("unsafe") {
//
//    it("zeros") {
//
//      val v = DoubleVector.unsafe.zeros(unstableFn)
//
//      assert(v.arity.runtimeValue == 3)
//    }
//  }
}
