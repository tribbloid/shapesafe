package org.shapesafe.core.arity.binary

import org.shapesafe.core.arity.Arity
import org.shapesafe.core.arity.Unchecked
import org.shapesafe.core.arity.ProveArity._

import scala.language.existentials

trait UncheckedDomain_Imp0 {

  case class D2[
      A1 <: Arity,
      A2 <: Arity,
      TC <: Arity
  ]()(
      implicit
      val bound1: A1 |-< Unchecked,
      val bound2: A2 |-< TC
  ) extends UncheckedDomain[A1, A2] {

    final type O1 = Unchecked

    final type Tightest = TC
    override def selectTightest(a1: A1, a2: A2): Tightest = bound2.instanceFor(a2)
  }

  implicit def d2[
      A1 <: Arity,
      A2 <: Arity,
      TC <: Arity
  ](
      implicit
      bound1: A1 |-< Unchecked,
      bound2: A2 |-< TC
  ): D2[A1, A2, TC] = D2()
}
