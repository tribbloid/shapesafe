package org.shapesafe.core.util;

import org.shapesafe.BaseSpec
import shapeless.{::, HNil, Witness};

class RecordUtilsSpec extends BaseSpec {

  import shapeless.syntax.singleton._

  val record = ("a" ->> 1) ::
    ("b" ->> "x") ::
    HNil

  it("getV") {

    object get extends RecordUtils.GetV(record)
//    val get = RecordUtils.GetV(record)
    // https://stackoverflow.com/questions/66036106/can-shapeless-record-type-be-used-as-a-poly1-part-2

    assert(get.apply("a".narrow) == 1)
    assert(get("b".narrow) == "x")

    assert(
      ("b".narrow :: "a".narrow :: HNil).map(get) ==
        ("x" :: 1 :: HNil)
    )
  }

  it("getField") {

    object get extends RecordUtils.GetField(record)

    assert(get.apply("a".narrow) == "a" ->> 1)
    assert(get.apply("b".narrow) == "b" ->> "x")

    assert(
      ("b".narrow :: "a".narrow :: HNil).map(get) ==
        ("b" ->> "x") ::
          ("a" ->> 1) ::
          HNil
    )
  }
}
