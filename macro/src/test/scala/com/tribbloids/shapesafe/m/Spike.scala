package com.tribbloids.shapesafe.m

import com.tribbloids.graph.commons.testlib.BaseSpec
import com.tribbloids.graph.commons.util.viz.VizType
import com.tribbloids.shapesafe.m.arity.Arity
import com.tribbloids.shapesafe.m.shape.Shape

class Spike extends BaseSpec {

  val shape = Shape ><
    Arity.FromLiteral(2) :<<- "x"

  val record = shape.static

  it("1") {

    VizType.infer(record).treeString.shouldBe()
  }

}
