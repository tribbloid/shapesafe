package com.tribbloids.shapesafe.m.tuple

import com.tribbloids.shapesafe.m.Poly1Group
import shapeless.{::, HList, HNil}

trait TupleSystem {

  type UpperBound

  type Impl

  val Eye: Impl
  type Eye = Eye.type

//  type ><[
//      TAIL <: Impl,
//      HEAD <: UpperBound
//  ] <: Impl

  // TODO:too cumbersome to define impl, switch to ~~> Proof pattern?
  trait Cons[-TAIL <: Impl, -HEAD <: UpperBound] {

    type Out <: Impl

    def apply(tail: TAIL, head: HEAD): Out
  }

  trait ToEye extends Poly1Group[HList, Impl] {

    implicit def toEye: HNil ==> Eye = { _ =>
      Eye
    }
  }

  object FromStatic extends ToEye {

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

object TupleSystem {}
