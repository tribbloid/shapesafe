package com.tribbloids.shapesafe.m.arity.spike

import com.tribbloids.graph.commons.util.viz.VizType
import com.tribbloids.shapesafe.BaseSpec
import com.tribbloids.shapesafe.m.arity.Arity
import org.scalatest.Ignore
import shapeless.Witness
import singleton.ops.+

@Ignore // TODO: looks like it is solved in the latest compiler? What's the difference to [[ErrorCase0]]
class AblativeSpike0 extends BaseSpec {

  val a = Arity(6)
//  val aa: Arity.FromLiteral[Witness.`6`.T] = a.out
  val aa = a.out

  val aViz = VizType[a.type]

  def assert_aViz_==(v: VizType): Unit = {

    val Seq(s1, s2) = Seq(aViz, v).map { v =>
      v.toString.split("\n").slice(1, Int.MaxValue).mkString("\n")
    }

    assert(s1 == s2)
  }

  implicitly[a.SS + Witness.`3`.T]
  implicitly[aa.SS + Witness.`3`.T]

  it("types should be identical") {

    assert_aViz_==(VizType[a.type])
    assert_aViz_==(VizType[aa.type])
  }
}
