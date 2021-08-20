package org.shapesafe.core.tuple

import shapeless.{::, HList}

trait CanFromStatic extends CanCons {
  _self: TupleSystem =>

  object FromStatic extends AbstractFromHList {

    implicit def inductive[
        H_TAIL <: HList,
        TAIL <: Tuple,
        HEAD <: VBound
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
