package shapesafe.m.viz

import ai.acyclic.prover.commons.testlib.BaseSpec

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
