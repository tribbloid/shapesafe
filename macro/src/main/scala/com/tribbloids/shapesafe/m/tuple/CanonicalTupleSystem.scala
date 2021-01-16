package com.tribbloids.shapesafe.m.tuple

import com.tribbloids.graph.commons.util.IDMixin
import shapeless.{::, HList, HNil}

import scala.language.implicitConversions

trait CanonicalTupleSystem[UB] extends TupleSystem {

  type Upper = UB

  trait Impl extends IDMixin {

    type Static <: HList
    def static: Static

    def asList: List[UB]

    override protected def _id: Any = asList
  }

//  object Impl {
//
//    implicit def toOps[SELF <: Impl](self: SELF)(
//        implicit canOps: SELF => Ops[SELF]
//    ): Ops[SELF] = canOps(self)
//  }

//  type Ops[SELF <: Impl]

  object Eye extends Impl {

    override type Static = HNil
    override def static: HNil = HNil

    override def asList: List[UB] = Nil

    override lazy val toString = "Eye"
  }

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
}

object CanonicalTupleSystem {

//  implicit def toEyeOps(s: TupleSystem[_]): s.Impl.InfixOps[s.Eye] = new s.Impl.InfixOps(s.Eye)
}
