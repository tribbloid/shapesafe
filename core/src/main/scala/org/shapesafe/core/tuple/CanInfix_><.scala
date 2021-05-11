package org.shapesafe.core.tuple

import scala.language.implicitConversions

trait CanInfix_>< extends CanCons {
  _self: TupleSystem =>

  trait InfixMixin[SELF <: Tuple] {

    def self: SELF

    def ><[
        HEAD <: UpperBound
    ](
        head: HEAD
    )(
        implicit
        cons: Cons[SELF, HEAD]
    ): cons.ConsResult = cons(self, head)
  }

  implicit class Infix[SELF <: Tuple](val self: SELF) extends InfixMixin[SELF] {}

  implicit def toEyeInfix(s: this.type): Infix[Eye] = Infix[Eye](Eye)
}

object CanInfix_>< {}
