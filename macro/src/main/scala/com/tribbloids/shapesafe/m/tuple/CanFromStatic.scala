package com.tribbloids.shapesafe.m.tuple

import com.tribbloids.shapesafe.m.Poly1Group
import shapeless.{::, HList, HNil}

trait CanFromStatic extends CanCons {
  _self: TupleSystem =>

  trait HListConverter extends Poly1Group[HList, Impl] {

    implicit def toEye: HNil ==> Eye = Builder[HNil].build { _: HNil =>
      Eye
    }
  }

  object FromStatic extends HListConverter {

    implicit def recursive[
        TAIL <: HList,
        PREV <: Impl,
        HEAD <: UpperBound
    ](
        implicit
        forTail: TAIL ==> PREV,
        cons: Cons[PREV, HEAD]
    ): (HEAD :: TAIL) ==> cons.Out = {

      { v: (HEAD :: TAIL) =>
        val prev = forTail(v.tail)

        cons(prev, v.head)
      }
    }
  }
}
