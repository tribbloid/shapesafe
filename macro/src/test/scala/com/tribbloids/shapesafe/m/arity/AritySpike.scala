package com.tribbloids.shapesafe.m.arity

import com.tribbloids.graph.commons.util.debug.print_@
import com.tribbloids.graph.commons.util.viz.VizType
import com.tribbloids.shapesafe.BaseSpec
import org.scalatest.Ignore

@Ignore
class AritySpike extends BaseSpec {

  it("narrow vs wide type") {

    val x1 = Arity(1)
    val y1 = Arity._1

    print_@(VizType.infer(x1))

    print_@(VizType[x1.type])

    print_@(VizType[y1.Wide])
  }

}
