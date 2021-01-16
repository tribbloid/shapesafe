package com.tribbloids.shapesafe.m.tuple

import scala.language.implicitConversions

trait CanInfix {
  _self: TupleSystem =>

  trait InfixMixin[SELF <: Impl] {

    def self: SELF

    def ><[
        HEAD <: Upper
    ](
        head: HEAD
    )(
        implicit canCross: CanCross[SELF, HEAD]
    ): ><[SELF, HEAD] = canCross(self, head)

    def cross[
        HEAD <: Upper
    ](
        head: HEAD
    )(
        implicit canCross: CanCross[SELF, HEAD]
    ): SELF >< HEAD = {

      ><(head)
    }
  }

  implicit class Infix[SELF <: Impl](val self: SELF) extends InfixMixin[SELF] {}

  implicit def toEyeInfix(s: this.type): Infix[s.Eye] = Infix(s.Eye)
}
