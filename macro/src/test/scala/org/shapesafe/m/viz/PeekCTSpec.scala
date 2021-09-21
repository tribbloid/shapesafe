package org.shapesafe.m.viz

class PeekCTSpec extends VizCTSpec {

  override val VizCT = PeekCT

  it("can format singleton") {

    VizRT[3].typeStr.shouldBe("3")
  }

  it("can format expressions of singletons") {

    trait ><[A, B]

    VizRT[3 >< 4 >< 5].typeStr.shouldBe("3 >< 4 >< 5")

    VizRT[3 >< (4 >< 5)].typeStr.shouldBe("3 >< (4 >< 5)")

    VizRT["_UNCHECKED_" >< (4 >< 5)].typeStr.shouldBe("_UNCHECKED_ >< (4 >< 5)")
  }
}
