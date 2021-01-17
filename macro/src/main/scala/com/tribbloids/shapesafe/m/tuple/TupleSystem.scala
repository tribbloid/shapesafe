package com.tribbloids.shapesafe.m.tuple

import com.tribbloids.shapesafe.m.Poly1Group
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

  trait FromEyeLike extends Poly1Group[HList, Impl] {

    implicit def toEye: HNil ==> Eye = { _ =>
      Eye
    }
  }

  object FromStatic extends FromEyeLike {

    implicit def recursive[
        TAIL <: HList,
        PREV <: Impl,
        HEAD <: UpperBound
    ](
        implicit
        forTail: TAIL ==> PREV,
        canCross: CanCross[PREV, HEAD]
    ): (HEAD :: TAIL) ==> (PREV >< HEAD) = {

      { v =>
        val prev = forTail(v.tail)

        canCross(prev, v.head)
      }
    }
  }
}
