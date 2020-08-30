package edu.umontreal.kotlingrad.shapesafe.core.tensor

import breeze.linalg.DenseVector
import edu.umontreal.kotlingrad.shapesafe.BaseSpec
import edu.umontreal.kotlingrad.shapesafe.m.arity.Arity
import graph.commons.util.ScalaReflection.universe.PolyType
import graph.commons.util.WideTyped
import graph.commons.util.debug.print_@
import graph.commons.util.viz.VizType
import shapeless.{HNil, ProductArgs, Witness}
import singleton.ops.+

class DoubleVectorSpec extends BaseSpec {

  val v0 = DoubleVector.random(6)

  {
    val result = v0.reify

    val aa = result.arity

    implicitly[aa.SS + Witness.`3`.T]
  }
}
