package org.shapesafe.core.shape

import org.shapesafe.graph.commons.testlib.BaseSpec
import org.shapesafe.m.viz.PeekCT

class IndexSpec extends BaseSpec {

  describe(classOf[Index.Name[_]].getSimpleName) {

    it("peek") {

      val ii = Index.Name("ABC")

      PeekCT.runtime[ii.Expr].typeStr.shouldBe("ABC")
    }
  }

  describe(classOf[Index.I_th[_, _]].getSimpleName) {

    it("peek") {

      val ii = Index.I_th(3)

      PeekCT.runtime[ii.Expr].typeStr.shouldBe("3")
    }
  }
}
