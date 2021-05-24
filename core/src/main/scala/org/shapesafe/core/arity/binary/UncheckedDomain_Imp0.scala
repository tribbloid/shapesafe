package org.shapesafe.core.arity.binary

import org.shapesafe.core.arity.Arity
import org.shapesafe.core.arity.LeafArity.Unchecked
import org.shapesafe.core.arity.ProveArity._

import scala.language.existentials

trait UncheckedDomain_Imp0 {

  case class D2[
      A1 <: Arity,
      A2 <: Arity,
      O <: Arity
  ]()(
      implicit
      val bound1: A1 |-< Unchecked,
      val bound2: A2 |-< O
  ) extends UncheckedDomain[A1, A2, O] {

    override def selectMoreSpecific(a1: A1, a2: A2): O = bound2.valueOf(a2)
  }

  implicit def d2[
      A1 <: Arity,
      A2 <: Arity,
      O <: Arity
  ](
      implicit
      bound1: A1 |-< Unchecked,
      bound2: A2 |-< O
  ): UncheckedDomain[A1, A2, O] = D2()
}
