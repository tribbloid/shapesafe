package org.shapesafe

import com.tribbloids.graph.commons.util.ScalaReflection
import com.tribbloids.graph.commons.util.reflect.TypeFormat
import com.tribbloids.graph.commons.util.viz.VizType
import shapeless.test.illTyped
import shapeless.{HList, Nat, Witness}

trait BaseSpec extends com.tribbloids.graph.commons.testlib.BaseSpec {

  /**
    * ScalaTest assertDoesNotCompile sometimes malfunction (due to unwashed execution order?)
    * & doesn't perform literal check
    * if the code compiles successfully, the project compilation will fail
    */
  val shouldNotCompile: illTyped.type = illTyped

  val small = BaseSpec.small
  val big = BaseSpec.big

  def showRaw(v: Any): String = {

    ScalaReflection.universe.showRaw(v)

//    pprint.tokenize(v).mkString("\n")
  }

  def typeInferShort[T: ScalaReflection.WeakTypeTag](v: T): String = {

    val format = TypeFormat(hidePackages = true, hideAlias = true)

    VizType
      .withFormat(format)
      .infer(v)
      .typeStr
  }
}

object BaseSpec {

  trait TypeN {

    val nat: Nat

    val hList: HList

    val w: Witness { type T <: Int }

    final def number = w.value
  }

  object small extends TypeN {

    val nat = Nat(6) // DO NOT change! most tests only use literals, they all needs to be consistent

    val hList = HList.fill[Double](nat)(0.0)

    val w = Witness(6)
  }

  /**
    * Nat uses church encoding and may risk slowing down compilation
    * as a result, we recommend tests using member of this object as much as possible
    */
  object big extends TypeN {

    // TODO: add type annotations, IDEA is really not smart enough to do this
    val nat = Nat(100) // DO NOT change! most tests only use literals, they all needs to be consistent
//    type Nat = nat.N

//    val num: Int = Nat.toInt(nat)

//    val hList = {
//
//      val s = 1 :: 2 :: 3 :: 4 :: 5 :: 6 :: 7 :: 8 :: 9 :: 0 :: HNil
//    }

    val hList = HList.fill[Double](nat)(0.0)

    val w = Witness(100)
//    type W = w.T
  }
}
