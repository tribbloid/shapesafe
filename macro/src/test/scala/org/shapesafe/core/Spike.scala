package org.shapesafe.core

import com.tribbloids.graph.commons.util.viz.VizType
import org.shapesafe.BaseSpec
import org.shapesafe.core.shape.Names
import org.scalatest.Ignore
import shapeless.{HNil, Witness}

@Ignore
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
    Names.Cons.summonFor(Names.Eye, v2)

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
