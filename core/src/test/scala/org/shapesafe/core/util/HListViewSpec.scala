package org.shapesafe.core.util;

import org.shapesafe.BaseSpec
import shapeless.HNil;

class HListViewSpec extends BaseSpec {

  import shapeless.syntax.singleton._

  val record = ("a" ->> 1) ::
    ("b" ->> "x") ::
    HNil
  val view = HListView(record)

  it(classOf[view.GetV].getSimpleName) {

    object poly extends view.GetV
//    val get = RecordUtils.GetV(record)
    // https://stackoverflow.com/questions/66036106/can-shapeless-record-type-be-used-as-a-poly1-part-2

    assert(poly.apply("a".narrow) == 1)
    assert(poly("b".narrow) == "x")

    assert(
      ("b".narrow :: "a".narrow :: HNil).map(poly) ==
        ("x" :: 1 :: HNil)
    )
  }

  it(classOf[view.GetField].getSimpleName) {

    object poly extends view.GetField

    // TODO: need type check
    assert(poly.apply("a".narrow) == "a" ->> 1)
    assert(poly.apply("b".narrow) == "b" ->> "x")

    assert(
      ("b".narrow :: "a".narrow :: HNil).map(poly) ==
        ("b" ->> "x") ::
        ("a" ->> 1) ::
        HNil
    )
  }
}
