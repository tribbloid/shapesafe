package org.shapesafe.core.tuple

import org.shapesafe.core.Poly1Base
import shapeless.{::, HList, HNil}

trait CanFromStatic extends CanCons {
  _self: TupleSystem =>

  trait HListConverter extends Poly1Base[HList, Impl] {

    implicit val toEye: HNil ==> Eye = {
      buildFrom[HNil].to { _ =>
        Eye
      }
    }
  }

  object FromStatic extends HListConverter {

    implicit def inductive[
        H_TAIL <: HList,
        TAIL <: Impl,
        HEAD <: UpperBound
    ](
        implicit
        forTail: H_TAIL ==> TAIL,
        cons: Cons[TAIL, HEAD]
    ): (HEAD :: H_TAIL) ==> cons.Out = {

      buildFrom[HEAD :: H_TAIL].to { v =>
        val prev = apply(v.tail)

        cons(prev, v.head)
      }
    }
  }
}
