package edu.umontreal.kotlingrad.shapesafe.m.arity

import edu.umontreal.kotlingrad.shapesafe.BaseSpec

trait OperandFixture extends BaseSpec {

  implicit val a = Arity(3)
  //    type A = a.type

  implicit val b = Arity(4)
  //    type B = b.type

  implicit val c = Arity(5)
  //    type C = c.type

  implicit val ab = Arity(7)

  implicit val abc = Arity(12)
}
