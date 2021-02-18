package org.shapesafe.core.tuple

import org.shapesafe.core.Poly1Base
import shapeless.{::, HList, HNil}

trait CanFromStatic extends CanCons {
  _self: TupleSystem =>

  trait AbstractFromHList extends Poly1Base[HList, Impl] {

    implicit val toEye: HNil ==> Eye = {
      from[HNil].==> { _ =>
        Eye
      }
    }

    trait From2 {}
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
    ): (HEAD :: H_TAIL) ==> cons.Out = {

      from[HEAD :: H_TAIL].==> { v =>
        val prev = apply(v.tail)

        cons(prev, v.head)
      }
    }
  }
}
