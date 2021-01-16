package com.tribbloids.shapesafe.m.tuple

import shapeless.{::, HList, HNil}

trait TupleSystem {

  type UpperBound

  type Impl

  val Eye: Impl
  final type Eye = Eye.type

  type ><[
      TAIL <: Impl,
      HEAD <: UpperBound
  ] <: Impl

  trait CanCross[TAIL <: Impl, HEAD <: UpperBound] {

    def apply(tail: TAIL, head: HEAD): ><[TAIL, HEAD]
  }

  trait FromStatic[I <: HList, O <: Impl] {

    def apply(in: I): O
  }
  object FromStatic {

    implicit def toEye: HNil FromStatic Eye = { _ =>
      Eye
    }

    implicit def recursive[
        TAIL <: HList,
        PREV <: Impl,
        HEAD <: UpperBound
    ](
        implicit
        forTail: TAIL FromStatic PREV,
        canCross: CanCross[PREV, HEAD]
    ): (HEAD :: TAIL) FromStatic (PREV >< HEAD) = {

      { v =>
        val prev = forTail(v.tail)

        canCross(prev, v.head)
      }
    }

    def apply[T <: HList, O <: Impl](v: T)(implicit ev: T FromStatic O): O = {

      ev.apply(v)
    }
  }
}
