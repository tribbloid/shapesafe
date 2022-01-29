package shapesafe

import ai.acyclic.graph.commons.testlib
import ai.acyclic.graph.commons.reflect.ScalaReflection
import ai.acyclic.graph.commons.viz.TypeViz
import shapesafe.m.viz.PeekCT
import shapeless.{HList, Nat, Witness}

trait BaseSpec extends testlib.BaseSpec with TypeViz.Fixtures {

  val small = BaseSpec.small
  val big = BaseSpec.big

  def showRaw(v: Any): String = {

    ScalaReflection.universe.showRaw(v)

//    pprint.tokenize(v).mkString("\n")
  }

  val TypeVizPeek = {
    PeekCT.runtime
  }

  def typeInferShort[T: ScalaReflection.WeakTypeTag](v: T = null.asInstanceOf[T]): String = {

    TypeVizShort
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
