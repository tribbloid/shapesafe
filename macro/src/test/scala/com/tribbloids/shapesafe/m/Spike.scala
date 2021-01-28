package com.tribbloids.shapesafe.m

import com.tribbloids.graph.commons.util.viz.VizType
import com.tribbloids.shapesafe.BaseSpec
import com.tribbloids.shapesafe.m.shape.Names
import shapeless.{HNil, Witness}

class Spike extends BaseSpec {

  import Spike._
  import shapeless.syntax.singleton._

  it("!") {
    val v1 = adhocW.value
    type VT = v1.type
    val v2: VT = v1

    VizType.infer(v1).shouldBe()
    VizType.infer(v2).shouldBe()

//    Names.Cons.summon(Names.Eye, v1) // breaks
    Names.Cons.peek(Names.Eye, v2)

    {
      val hh = v1 :: HNil
      VizType.infer(hh).shouldBe()
//      Names.FromStatic.apply(hh) // breaks
    }

    {
      val hh = "a".narrow :: HNil
      VizType.infer(hh).shouldBe()
//      Names.FromStatic.apply(hh) // breaks
    }

    {
      val hh = v2 :: HNil
      VizType.infer(hh).shouldBe()
      Names.FromStatic.apply(hh)
    }

  }
}

object Spike {

  def adhocW = Witness("a")

  val singletonW = Witness("a")
}
