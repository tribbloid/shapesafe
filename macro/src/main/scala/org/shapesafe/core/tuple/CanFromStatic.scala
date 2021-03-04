package org.shapesafe.core.tuple

import org.shapesafe.core.Poly1Base
import shapeless.ops.hlist.Reverse
import shapeless.{::, HList, HNil, NatProductArgs, SingletonProductArgs}

trait CanFromStatic extends CanCons {
  _self: TupleSystem =>

  trait AbstractFromHList extends Poly1Base[HList, Impl] {

    implicit val toEye: HNil ==> Eye = {
      forAll[HNil].==> { _ =>
        Eye
      }
    }

  }

  object FromStatic extends AbstractFromHList {

    implicit def inductive[
        H_TAIL <: HList,
        TAIL <: Impl,
        HEAD <: UpperBound
    ](
        implicit
        forTail: H_TAIL ==> TAIL,
        cons: Cons[TAIL, HEAD]
    ): (HEAD :: H_TAIL) ==> cons.ConsResult = {

      forAll[HEAD :: H_TAIL].==> { v =>
        val prev = apply(v.tail)

        cons(prev, v.head)
      }
    }
  }
}
