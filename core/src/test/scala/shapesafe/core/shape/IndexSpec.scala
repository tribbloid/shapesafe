package shapesafe.core.shape

import ai.acyclic.graph.commons.testlib.BaseSpec
import shapesafe.m.viz.PeekCT

class IndexSpec extends BaseSpec {

  describe(classOf[Index.Name[_]].getSimpleName) {

    it("peek") {

      val ii = Index.Name("ABC")

      PeekCT.runtime[ii.Notation].typeStr.shouldBe("ABC")
    }
  }

  describe(classOf[Index.LtoR[_, _]].getSimpleName) {

    it("peek") {

      val ii = Index.LtoR(3)

      PeekCT.runtime[ii.Notation].typeStr.shouldBe("3")
    }
  }
}
