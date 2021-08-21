package org.shapesafe.core.tuple

import shapeless.{::, HList}

import scala.language.implicitConversions

trait CanCons extends TupleSystem {

  type ><[TAIL <: Tuple, HEAD <: VBound] <: Tuple

  def cons[TAIL <: Tuple, HEAD <: VBound](tail: TAIL, head: HEAD): TAIL >< HEAD

  // TODO:too much boilerplate, switch to ~~> Proof pattern or Poly1/Poly2?
//  trait ConsLemma[-TAIL <: Tuple, -HEAD <: VBound] {
//
//    type ConsResult <: Tuple
//
//    def apply(tail: TAIL, head: HEAD): ConsResult
//  }
//
//  object ConsLemma {
//
//    def from[TAIL <: Tuple, HEAD <: VBound] = new Factory[TAIL, HEAD]
//
//    class Factory[TAIL <: Tuple, HEAD <: VBound] {
//
//      def to[O <: Tuple](fn: (TAIL, HEAD) => O) = new FromFn2[TAIL, HEAD, O](fn)
//    }
//
//    case class FromFn2[-TAIL <: Tuple, -HEAD <: VBound, O <: Tuple](
//        fn: (TAIL, HEAD) => O
//    ) extends ConsLemma[TAIL, HEAD] {
//
//      final type ConsResult = O
//
//      final override def apply(tail: TAIL, head: HEAD): ConsResult = fn(tail, head)
//    }
//
//    def summonFor[TAIL <: Tuple, HEAD <: VBound](tail: TAIL, head: HEAD)(
//        implicit
//        ev: ConsLemma[TAIL, HEAD]
//    ): ev.type = ev
//
//    def apply[TAIL <: Tuple, HEAD <: VBound](tail: TAIL, head: HEAD)(
//        implicit
//        ev: ConsLemma[TAIL, HEAD]
//    ): ev.ConsResult = {
//
//      ev.apply(tail, head)
//    }
//  }

  trait ConsIntake[B <: VBound] extends HListIntake {

    implicit def inductive[
        H_TAIL <: HList,
        TAIL <: Tuple,
        HEAD <: B
    ](
        implicit
        forTail: H_TAIL ==> TAIL
    ): (HEAD :: H_TAIL) ==> ><[TAIL, HEAD] = {

      forAll[HEAD :: H_TAIL].==> { v =>
        val prev = apply(v.tail)

        cons(prev, v.head)
      }
    }
  }

  object FromStatic extends ConsIntake[VBound] {}

  object FromLiterals extends ConsIntake[VBound with Singleton] {}

  trait InfixMixin[SELF <: Tuple] {

    def self: SELF

    def ><[
        HEAD <: VBound
    ](
        head: HEAD
    ): SELF >< HEAD = cons(self, head)
  }

  implicit class InfixFunctions[SELF <: Tuple](val self: SELF) extends InfixMixin[SELF] {}

  implicit def toEyeInfix(s: this.type): InfixFunctions[Eye] = InfixFunctions[Eye](Eye)
}
