package org.shapesafe.m.viz

class PeekCTSpec extends VizCTSpec {

  override val VizCT = PeekCT

  it("can format singleton") {

    VizRT[3].typeStr.shouldBe("3")
  }

  trait ><[A, B]

  it("can format expressions of singletons") {

    VizRT[3 >< 4 >< 5].typeStr.shouldBe("3 >< 4 >< 5")

    VizRT[3 >< (4 >< 5)].typeStr.shouldBe("3 >< (4 >< 5)")

    VizRT["_UNCHECKED_" >< (4 >< 5)].typeStr.shouldBe("_UNCHECKED_ >< (4 >< 5)")
  }

  it(" ... with duplicate(S) in type args") {

    VizRT[4 >< 4].typeStr.shouldBe("4 >< 4")

    VizRT[3 >< (4 >< 4)].typeStr.shouldBe("3 >< (4 >< 4)")

    VizRT["i" >< ("j" >< "j")].typeStr.shouldBe("i >< (j >< j)")
  }
}
