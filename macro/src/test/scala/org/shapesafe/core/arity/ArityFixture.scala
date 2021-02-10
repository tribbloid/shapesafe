package org.shapesafe.core.arity

import org.shapesafe.BaseSpec

trait ArityFixture extends BaseSpec {

  implicit val a = Leaf(3)
  //    type A = a.type

  implicit val b = Leaf(4)
  //    type B = b.type

  implicit val c = Leaf(5)
  //    type C = c.type

  implicit val ab = Leaf(7)

  implicit val abc = Leaf(12)
}
