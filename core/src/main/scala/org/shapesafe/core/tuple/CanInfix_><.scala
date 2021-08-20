package org.shapesafe.core.tuple

import scala.language.implicitConversions

trait CanInfix_>< extends CanCons {

  trait InfixMixin[SELF <: Tuple] {

    def self: SELF

    def ><[
        HEAD <: VBound
    ](
        head: HEAD
    )(
        implicit
        cons: ConsLemma[SELF, HEAD]
    ): cons.ConsResult = cons(self, head)
  }

  implicit class Infix[SELF <: Tuple](val self: SELF) extends InfixMixin[SELF] {}

  implicit def toEyeInfix(s: this.type): Infix[Eye] = Infix[Eye](Eye)
}

object CanInfix_>< {}
