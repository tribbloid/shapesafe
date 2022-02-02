package shapesafe.core

import shapesafe.BaseSpec

class ConstSpec extends BaseSpec {

  it("can summon True") {

    implicitly[Const.True]
  }
}
