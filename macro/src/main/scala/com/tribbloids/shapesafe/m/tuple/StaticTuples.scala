package com.tribbloids.shapesafe.m.tuple

import com.tribbloids.graph.commons.util.IDMixin
import shapeless.{::, HList, HNil}

import scala.language.implicitConversions

trait StaticTuples[UB] extends TupleSystem with CanFromStatic {

  final type UpperBound = UB

  trait Impl extends IDMixin {

    type Static <: HList
    def static: Static

    def asList: List[UB]

    override protected def _id: Any = asList
  }

  trait EyeLike extends Impl {

    override type Static = HNil
    override def static: HNil = HNil

    override def asList: List[UB] = Nil

    override lazy val toString = "Eye"
  }
  final object eye extends EyeLike

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

object StaticTuples {

//  implicit def toEyeOps(s: TupleSystem[_]): s.Impl.InfixOps[s.Eye] = new s.Impl.InfixOps(s.Eye)

  trait Total[UB] extends StaticTuples[UB] {

    implicit def consAlways[TAIL <: Impl, HEAD <: UB]: Cons.FromFn[TAIL, HEAD, TAIL >< HEAD] = {

      Cons[TAIL, HEAD].to { (tail, head) =>
        new ><(tail, head)
      }
    }
  }
}
