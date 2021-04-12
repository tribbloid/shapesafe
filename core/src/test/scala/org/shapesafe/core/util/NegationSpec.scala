package org.shapesafe.core.util

import com.tribbloids.graph.commons.util.viz.TypeViz
import org.shapesafe.BaseSpec

class NegationSpec extends BaseSpec {

  describe("can prove") {

    val t1 = TypeViz[~[Int <:< AnyRef]]

    it(t1.tt.toString) {

      implicitly[t1.TT]
    }
  }

  describe("cannot prove") {

    val t1 = TypeViz[~[String <:< AnyRef]]

    it(t1.tt.toString) {

      shouldNotCompile(
        """implicitly[t1.TT]""",
        ".*"
      )
    }
  }
}
