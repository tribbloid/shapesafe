package com.tribbloids.shapesafe.m.shape

import com.tribbloids.graph.commons.util.viz.VizType
import com.tribbloids.shapesafe.BaseSpec
import shapeless.{::, HNil, Witness}

class NamesSpec extends BaseSpec {

  import shapeless.syntax.singleton._

  val names = Names >< "x" >< "y" >< "z"

  val hList = "z".narrow :: "y".narrow :: "x".narrow :: HNil

  it("create") {

    require(names.static == hList)

    val t1 = VizType.infer(names.static)
    val t2 = VizType.infer(hList)

    assert(t1.tt =:= t2.tt)
  }

  it("cons") {

    shouldNotCompile(
      """implicitly[Names.Cons[Names.Eye, String]]"""
    )

    val w = Witness("a")
    implicitly[Names.Cons[Names.Eye, w.T]]

    val hh = "a".narrow :: HNil

    Names.FromStatic.apply(hh)

//    Names.FromStatic.apply("a" :: HNil)
  }

  it("FromStatic") {

    val names2 = Names.FromStatic(hList)

    // TODO: runtime assertion?

    val t1 = VizType.infer(names)
    val t2 = VizType.infer(names2)

    t1.shouldBe(t2)
  }
}
