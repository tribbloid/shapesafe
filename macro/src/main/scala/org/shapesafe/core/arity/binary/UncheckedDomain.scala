package org.shapesafe.core.arity.binary

import org.shapesafe.core.arity.LeafArity.Unchecked
import org.shapesafe.core.arity.ProveArity._
import org.shapesafe.core.arity.Utils.Op
import org.shapesafe.core.arity.ops.ArityOps.=!=
import org.shapesafe.core.arity.{Arity, ProveArity, Utils}

import scala.language.existentials

abstract class UncheckedDomain[
    A1 <: Arity,
    A2 <: Arity,
    O <: Term
] {

  def bound1: A1 =>>^^ Term
  def bound2: A2 =>>^^ Term

  def selectSafer(a1: A1, a2: A2): O

  def forOp2[??[X1, X2] <: Op](
      implicit
      sh: Utils.IntSh[??]
  ): Op2[??]#On[A1, A2] =>> Unchecked = ProveArity.forAll[Op2[??]#On[A1, A2]].=>> { _ =>
    Unchecked
  }

  val forEqual: A1 =!= A2 =>>^^ O = ProveArity.forAll[A1 =!= A2].=>>^^ { v =>
    selectSafer(v.a1, v.a2)
  }
}

trait UncheckedDomain_Imp0 {

  case class D2[
      A1 <: Arity,
      A2 <: Arity,
      O <: Term
  ]()(
      implicit
      val bound1: A1 |~~ Unchecked,
      val bound2: A2 =>>^^ O
  ) extends UncheckedDomain[A1, A2, O] {

    override def selectSafer(a1: A1, a2: A2): O = bound2(a2)
  }

  implicit def d2[
      A1 <: Arity,
      A2 <: Arity,
      O <: Term
  ](
      implicit
      bound1: A1 |~~ Unchecked,
      bound2: A2 =>>^^ O
  ): UncheckedDomain[A1, A2, O] = D2()
}

object UncheckedDomain extends UncheckedDomain_Imp0 {

  def summon[
      A1 <: Arity,
      A2 <: Arity,
      O <: Term
  ](
      implicit
      self: UncheckedDomain[A1, A2, O]
  ): UncheckedDomain[A1, A2, O] = self

  case class D1[
      A1 <: Arity,
      A2 <: Arity,
      O <: Term
  ]()(
      implicit
      val bound1: A1 =>>^^ O,
      val bound2: A2 |~~ Unchecked
  ) extends UncheckedDomain[A1, A2, O] {

    override def selectSafer(a1: A1, a2: A2): O = bound1(a1)
  }

  implicit def d1[
      A1 <: Arity,
      A2 <: Arity,
      O <: Term
  ](
      implicit
      bound1: A1 =>>^^ O,
      bound2: A2 |~~ Unchecked
  ): UncheckedDomain[A1, A2, O] = D1()
}
