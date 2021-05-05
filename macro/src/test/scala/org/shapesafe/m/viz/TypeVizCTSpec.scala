package org.shapesafe.m.viz

import com.tribbloids.graph.commons.testlib.BaseSpec

class TypeVizCTSpec extends BaseSpec {

  describe("can emit error for") {

    it("Int") {
      val viz = TypeVizCT[Int]

      shouldNotCompile(
        """viz.interrupt""",
        """.*(Int).*"""
      )
    }

    it("6") {

      val viz = TypeVizCT[6]

      shouldNotCompile(
        """viz.interrupt""",
        """.*(6).*"""
      )
    }
  }
}
