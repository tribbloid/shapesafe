package edu.umontreal.kotlingrad.shapesafe.m.arity

import com.tribbloids.graph.commons.util.viz.VizType
import edu.umontreal.kotlingrad.shapesafe.BaseSpec
import shapeless.Witness
import singleton.ops.+

class ArityAblative extends BaseSpec {

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

  it("stripped to the core") {

    {
      assert_aViz_==(VizType[a.type])

      implicitly[a.SS + Witness.`3`.T]
    }

    {
      assert_aViz_==(VizType[aa.type])

      implicitly[aa.SS + Witness.`3`.T]
    }
  }
}
