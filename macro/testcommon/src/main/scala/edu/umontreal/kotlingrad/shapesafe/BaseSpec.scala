package edu.umontreal.kotlingrad.shapesafe

import org.scalatest.FunSpec
import shapeless.test.illTyped
import shapeless.{HList, Nat, Witness}

//TODO: merge into :graph-commons:testlib
abstract class BaseSpec extends FunSpec {

  /**
    * ScalaTest assertDoesNotCompile sometimes malfunction (due to unwashed execution order?)
    * & doesn't perform literal check
    * if the code compiles successfully, the project compilation will fail
    */
  val shouldNotCompile: illTyped.type = illTyped

  val big: BaseSpec.big.type = BaseSpec.big
}

object BaseSpec {

  /**
    * Nat uses church encoding and may risk slowing down compilation
    * as a result, we recommend tests using member of this object as much as possible
    */
  object big {

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
