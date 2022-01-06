package shapesafe.core.arity

import shapesafe.BaseSpec

trait ArityFixture extends BaseSpec {

  implicit val a = Arity(3)
  //    type A = a.type

  implicit val b = Arity(4)
  //    type B = b.type

  implicit val c = Arity(5)
  //    type C = c.type

  implicit val ab = Arity(7)

  implicit val abc = Arity(12)
}
