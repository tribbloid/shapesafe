package org.shapesafe.core.arity

import org.shapesafe.BaseSpec

trait ArityFixture extends BaseSpec {

  implicit val a = LeafArity(3)
  //    type A = a.type

  implicit val b = LeafArity(4)
  //    type B = b.type

  implicit val c = LeafArity(5)
  //    type C = c.type

  implicit val ab = LeafArity(7)

  implicit val abc = LeafArity(12)
}
