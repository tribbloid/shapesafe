package org.shapesafe.core.arity.binary

import org.shapesafe.core.arity.LeafArity.Unchecked
import org.shapesafe.core.arity.ProveArity._
import org.shapesafe.core.arity.Utils.Op
import org.shapesafe.core.arity.{Arity, LeafArity, Utils}

import scala.language.{existentials, higherKinds}

abstract class UncheckedDomain[
    A1 <: Arity,
    A2 <: Arity,
    O <: Proof
] {

  import org.shapesafe.core.arity.Syntax._

  def bound1: A1 =>> Proof
  def bound2: A2 =>> Proof

  def selectSafer(a1: A1, a2: A2): O

  case class ForOp2[??[X1, X2] <: Op]()(
      implicit
      sh: Utils.IntSh[??]
  ) extends (Op2[??]#On[A1, A2] =>> OfUnchecked) {

    implicit class apply(in: Op2[??]#On[A1, A2]) extends OfUnchecked {

      override def out = {

        LeafArity.Unchecked
      }
    }
  }

  case object ForEqual extends (A1 =!= A2 =>> O) {

    def apply(in: A1 =!= A2): O = selectSafer(in.a1, in.a2)
  }
  type ForEqual = ForEqual.type
}

trait UncheckedDomain_Imp0 {

  case class D2[
      A1 <: Arity,
      A2 <: Arity,
      O <: Proof
  ]()(
      implicit
      val bound1: A1 ~~> Unchecked,
      val bound2: A2 =>> O
  ) extends UncheckedDomain[A1, A2, O] {

    override def selectSafer(a1: A1, a2: A2): O = bound2(a2)
  }

  implicit def d2[
      A1 <: Arity,
      A2 <: Arity,
      O <: Proof
  ](
      implicit
      bound1: A1 ~~> Unchecked,
      bound2: A2 =>> O
  ): UncheckedDomain[A1, A2, O] = D2()
}

object UncheckedDomain extends UncheckedDomain_Imp0 {

  def summon[
      A1 <: Arity,
      A2 <: Arity,
      O <: Proof
  ](
      implicit
      self: UncheckedDomain[A1, A2, O]
  ): UncheckedDomain[A1, A2, O] = self

  case class D1[
      A1 <: Arity,
      A2 <: Arity,
      O <: Proof
  ]()(
      implicit
      val bound1: A1 =>> O,
      val bound2: A2 ~~> Unchecked
  ) extends UncheckedDomain[A1, A2, O] {

    override def selectSafer(a1: A1, a2: A2): O = bound1(a1)
  }

  implicit def d1[
      A1 <: Arity,
      A2 <: Arity,
      O <: Proof
  ](
      implicit
      bound1: A1 =>> O,
      bound2: A2 ~~> Unchecked
  ): UncheckedDomain[A1, A2, O] = D1()
}
