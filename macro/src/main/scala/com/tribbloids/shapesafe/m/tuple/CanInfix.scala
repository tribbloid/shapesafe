package com.tribbloids.shapesafe.m.tuple

import scala.language.implicitConversions

trait CanInfix[UB] {
  _self: TupleSystem[UB] =>

  trait InfixOpsMixin[SELF <: Impl] {

    def self: SELF

    def ><[
        HEAD <: UB
    ](
        head: HEAD
    )(
        implicit canCross: CanCross[SELF, HEAD]
    ): ><[SELF, HEAD] = canCross(self, head)

    def cross[
        HEAD <: UB
    ](
        head: HEAD
    )(
        implicit canCross: CanCross[SELF, HEAD]
    ): SELF >< HEAD = {

      ><(head)
    }
  }

  implicit class Ops[SELF <: Impl](val self: SELF) extends InfixOpsMixin[SELF] {}

  implicit def toEyeOps(s: this.type): Ops[s.Eye] = Ops(s.Eye)
}
