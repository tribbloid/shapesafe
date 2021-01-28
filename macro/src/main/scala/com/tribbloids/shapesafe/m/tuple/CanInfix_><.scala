package com.tribbloids.shapesafe.m.tuple

import scala.language.implicitConversions

trait CanInfix_>< extends CanCons {
  _self: TupleSystem =>

  trait InfixMixin[SELF <: Impl] {

    def self: SELF

    def ><[
        HEAD <: UpperBound
    ](
        head: HEAD
    )(
        implicit cons: Cons[SELF, HEAD]
    ): cons.Out = cons(self, head)

    def cross[
        HEAD <: UpperBound
    ](
        head: HEAD
    )(
        implicit cons: Cons[SELF, HEAD]
    ) = {

      ><(head)
    }
  }

  implicit class Infix[SELF <: Impl](val self: SELF) extends InfixMixin[SELF] {}

  implicit def toEyeInfix(s: this.type): Infix[Eye] = Infix[Eye](Eye)
}

object CanInfix_>< {}
