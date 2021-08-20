package org.shapesafe.core.tuple

import shapeless.{::, HList}

trait CanCons extends TupleSystem {

  // TODO:too much boilerplate, switch to ~~> Proof pattern or Poly1/Poly2?
  trait ConsLemma[-TAIL <: Tuple, -HEAD <: VBound] {

    type ConsResult <: Tuple

    def apply(tail: TAIL, head: HEAD): ConsResult
  }

  object ConsLemma {

    def from[TAIL <: Tuple, HEAD <: VBound] = new Factory[TAIL, HEAD]

    class Factory[TAIL <: Tuple, HEAD <: VBound] {

      def to[O <: Tuple](fn: (TAIL, HEAD) => O) = new FromFn2[TAIL, HEAD, O](fn)
    }

    case class FromFn2[-TAIL <: Tuple, -HEAD <: VBound, O <: Tuple](
        fn: (TAIL, HEAD) => O
    ) extends ConsLemma[TAIL, HEAD] {

      final type ConsResult = O

      final override def apply(tail: TAIL, head: HEAD): ConsResult = fn(tail, head)
    }

    def summonFor[TAIL <: Tuple, HEAD <: VBound](tail: TAIL, head: HEAD)(
        implicit
        ev: ConsLemma[TAIL, HEAD]
    ): ev.type = ev

    def apply[TAIL <: Tuple, HEAD <: VBound](tail: TAIL, head: HEAD)(
        implicit
        ev: ConsLemma[TAIL, HEAD]
    ): ev.ConsResult = {

      ev.apply(tail, head)
    }
  }

  trait ConsIntake[ELEM <: VBound] extends HListIntake {

    implicit def inductive[
        H_TAIL <: HList,
        TAIL <: Tuple,
        HEAD <: ELEM
    ](
        implicit
        forTail: H_TAIL ==> TAIL,
        cons: ConsLemma[TAIL, HEAD]
    ): (HEAD :: H_TAIL) ==> cons.ConsResult = {

      forAll[HEAD :: H_TAIL].==> { v =>
        val prev = apply(v.tail)

        cons(prev, v.head)
      }
    }
  }

  object FromStatic extends ConsIntake[VBound] {}

  object FromLiterals extends ConsIntake[VBound with Singleton] {}
}
