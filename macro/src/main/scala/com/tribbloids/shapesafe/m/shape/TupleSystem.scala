package com.tribbloids.shapesafe.m.shape

import shapeless.{::, HList, HNil}

import scala.language.implicitConversions

trait TupleSystem[UB] {

  trait Impl {

    type Static <: HList
    def static: Static
  }

  object Impl {

    implicit def toOps[SELF <: Impl](self: SELF): Ops[SELF] = getOps(self)
  }

  def getOps[SELF <: Impl](self: SELF): Ops[SELF]
  type Ops[SELF <: Impl]

  trait InfixOpsMixin[SELF <: Impl] {

    def self: SELF

    def ><[
        HEAD <: UB
    ](
        head: HEAD
    ): ><[SELF, HEAD] = new ><(self, head)

    def cross[
        HEAD <: UB
    ](
        head: HEAD
    ): SELF >< HEAD = {

      ><(head)
    }
  }

  class Eye extends Impl {

    override type Static = HNil
    override def static: HNil = HNil
  }
  val Eye = new Eye()

  // cartesian product symbol
  class ><[
      TAIL <: Impl,
      HEAD <: UB
  ](
      val tail: TAIL,
      val head: HEAD
  ) extends Impl {

    override type Static = HEAD :: tail.Static
    override def static: Static = head :: tail.static
  }

  object FromStatic {

    trait ~~>[I <: HList, O <: Impl] {

      def apply(in: I): O
    }

    implicit def toEye: HNil ~~> Eye = { _ =>
      Eye
    }

    implicit def recursive[
        TAIL <: HList,
        PREV <: Impl,
        HEAD <: UB
    ](
        implicit
        forTail: TAIL ~~> PREV
    ): (HEAD :: TAIL) ~~> (PREV >< HEAD) = {

      { v =>
        val prev = forTail(v.tail)

        new ><(prev, v.head)
      }
    }

    def apply[T <: HList, O <: Impl](v: T)(implicit ev: T ~~> O): O = {

      ev.apply(v)
    }
  }

}

object TupleSystem {

//  implicit def toEyeOps(s: TupleSystem[_]): s.Impl.InfixOps[s.Eye] = new s.Impl.InfixOps(s.Eye)
}
