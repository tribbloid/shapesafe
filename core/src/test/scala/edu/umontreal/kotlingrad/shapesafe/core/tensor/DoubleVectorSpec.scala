package edu.umontreal.kotlingrad.shapesafe.core.tensor

import breeze.linalg.DenseVector
import edu.umontreal.kotlingrad.shapesafe.BaseSpec
import edu.umontreal.kotlingrad.shapesafe.m.arity.Arity
import graph.commons.util.WideTyped
import graph.commons.util.debug.print_@
import graph.commons.util.viz.VizType
import shapeless.{HNil, ProductArgs, Witness}

class DoubleVectorSpec extends BaseSpec {

  it("apply") {

    val v = DoubleVector(1.0, 2.0, 3.0)

    v.shape.internal.requireEqual(3)
  }

  it("... with YUUGE number of args!") {

    val v = DoubleVector(
      1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 0.0, //
      1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 0.0, //
      1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 0.0, //
      1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 0.0, //
      1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 0.0 //
    ) // TODO: scalafmt in IDEA failed here

    v.shape.internal.requireEqual(50)
  }

  it(
    s"... won't cause ${classOf[ProductArgs].getSimpleName}.applyDynamic to contaminate compile-time method validation") {

    shouldNotCompile(
      "DoubleVector.dummy(1.0)"
    )
  }

  describe("from") {
    it("hList") {

      val v = DoubleVector.from.hList(1.0 :: 2.0 :: 3.0 :: HNil)

      v.shape.internal.requireEqual(3)
    }
  }

  val stable: Int = 1 + 2 // stable path!
  lazy val stableLzy: Int = 1 + 2 // stable path!

  def unstableFn: Int = 1 + 2 // no path
  var unstableVar: Int = 1 + 2 // no path

  describe("zeros") {

    it("(literal)") {
      val v0 = DoubleVector.zeros(3)

      //    val v0type = ScalaReflection.universe.typeOf[v0.arity.type ].finalResultType
      //    println(v0type)

      v0.shape.internal.proveSame[Witness.`3`.T]
      v0.shape.internal.proveEqual[Witness.`3`.T]
      v0.shape.internal.requireEqual(3)

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
      val w3 = Witness(3)
      type A3 = Arity.FromLiteral[w3.T]

      val w4 = Witness(4)
      type A4 = Arity.FromLiteral[w4.T]

      val v1: DoubleVector[A3] = DoubleVector.zeros(3)
      val v2: DoubleVector[A3] = DoubleVector.zeros(3)
      val v3: DoubleVector[A4] = DoubleVector.zeros(4)

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
      result.shape.internal.requireEqual(5)

      val v2 = DoubleVector.zeros(5)
      val v3 = DoubleVector.zeros(6)

      assert((result dot_* v2) == 0.0)

      shouldNotCompile {
        "result dot_* v3"
      }
    }
  }

  describe("pad") {

    it("spike 1") {

      val v0 = DoubleVector.random(6)
      val v1 = v0.pad(3)

      {
        val result = v1.reify

        val aa = result.arity

        {
          print_@(WideTyped(result.arity).viz)
          // this works
          result.crossValidate()
          result.arity.internal.requireEqual(12)
        }

        {
          print_@(WideTyped(aa).viz)
          // this doesn't, how did it happened?

          val ss = aa.single
          println(VizType[aa.type])

//          aa.internal.dummyImp(3)
//          aa.internal.requireEqual(12)
        }
      }

    }

//    it("spike 2") {
//
//      val v0 = DoubleVector.random(6)
//      val v1 = v0.pad(3)
//
//      {
//        val aa = DoubleVector.asReified(v0).arity
//
//        print_@(WideTyped(aa).viz)
//        aa.internal.dummyImp(3)
//      }
//
//      {
//        val aa = v0.arity
//
//        print_@(WideTyped(aa).viz)
//        aa.internal.dummyImp(3)
//      }
//
//      {
//        val aa = DoubleVector.asReified(v1)
//
//        print_@(WideTyped(aa).viz)
//      }
//      {
//        val aa = v1.arity
//
//        print_@(WideTyped(aa).viz)
//      }
//    }

    it("1") {

      val v0 = DoubleVector.random(6)

      val result = v0.pad(3).reify

      result.crossValidate()
      result.arity.internal.requireEqual(12)
    }
  }

  describe("conv") {

    it("1") {

      val v0 = DoubleVector.random(6)
      val v1 = DoubleVector.random(3)

      val result = v0.conv(v1)
      result.crossValidate()
      result.arity.internal.requireEqual(4)
    }

    it("2") {

      val v0 = DoubleVector.random(6)
      val v1 = DoubleVector.random(3)
      val v2 = DoubleVector.random(2)

      {
        val result = v0.conv(v1, 2)
        result.crossValidate()
        result.arity.internal.requireEqual(2)
      }

      {
        val result = v0.conv(v2, 2)
        result.crossValidate()
        result.arity.internal.requireEqual(2)
      }
    }
  }

  describe("unsafe") {

    it("zeros") {

      val v = DoubleVector.unsafe.zeros(unstableFn)

      assert(v.shape.number == 3)
    }
  }
}
