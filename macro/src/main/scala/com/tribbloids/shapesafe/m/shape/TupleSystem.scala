package com.tribbloids.shapesafe.m.shape

import com.tribbloids.graph.commons.util.IDMixin
import shapeless.{::, HList, HNil}

import scala.language.implicitConversions

trait TupleSystem[UB] {

  trait Impl extends IDMixin {

    type Static <: HList
    def static: Static

    def asList: List[UB]

    override protected def _id: Any = asList
  }

  object Impl {

    implicit def toOps[SELF <: Impl](self: SELF): Ops[SELF] = getOps(self)
  }

  type Ops[SELF <: Impl]
  def getOps[SELF <: Impl](self: SELF): Ops[SELF]

  trait InfixOpsMixin[SELF <: Impl] {

    def self: SELF

    def ><[
        HEAD <: UB
    ](
        head: HEAD
    ): ><[SELF, HEAD] = TupleSystem.this.><(self, head)

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

    override def asList: List[UB] = Nil

    override lazy val toString = "Eye"
  }
  val Eye: Eye = new Eye()

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

    override def asList: List[UB] = tail.asList ++ Seq(head)

    override lazy val toString = s"${tail.toString} >< $head"
  }
  protected def ><[
      TAIL <: Impl,
      HEAD <: UB
  ](
      tail: TAIL,
      head: HEAD
  ): ><[TAIL, HEAD] = new ><(tail, head)

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

        ><(prev, v.head)
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
