package com.tribbloids.shapesafe.m.shape

import com.tribbloids.graph.commons.util.viz.VizType
import com.tribbloids.shapesafe.BaseSpec
import com.tribbloids.shapesafe.m.shape.Names.Impl._
import shapeless.HNil

class NamesSuite extends BaseSpec {

  import shapeless.syntax.singleton._

  val names = Names >< "x" >< "y" >< "z"

  val hList = "z".narrow :: "y".narrow :: "x".narrow :: HNil

  it("create") {

    require(names.static == hList)

    val t1 = VizType.infer(names.static)
    val t2 = VizType.infer(hList)

    assert(t1.tt =:= t2.tt)
  }

  it("FromStatic") {

    val names2 = Names.FromStatic(hList)

    // TODO: runtime assertion?

    val t1 = VizType.infer(names)
    val t2 = VizType.infer(names2)

//    t1.toString.shouldBe()

    assert(t1.tt =:= t2.tt)
  }
}
