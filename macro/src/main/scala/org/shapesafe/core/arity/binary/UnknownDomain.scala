package org.shapesafe.core.arity.binary

import org.shapesafe.core.arity.LeafArity.Unknown
import org.shapesafe.core.arity.ProveArity._
import org.shapesafe.core.arity.Utils.Op
import org.shapesafe.core.arity.{Arity, LeafArity, Utils}

import scala.language.{existentials, higherKinds}

abstract class UnknownDomain[
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
  ) extends (Op2[??]#On[A1, A2] =>> OfUnknown) {

    implicit class apply(in: Op2[??]#On[A1, A2]) extends OfUnknown {

      override def out = {

//        val n1 = bound1.apply(in.a1).out.numberOpt
//        val n2 = bound2.apply(in.a2).out.numberOpt
//
//        (n1, n2) match {
//
//          case (Some(_1), Some(_2)) =>
//            val nOut = tfs.apply(n1.get, n2.get).getValue
//            Known.Dynamic(nOut)
//          case _ =>
//            Known.Unknown
//        }

        LeafArity.Unknown
      }
    }
  }

  case object ForEqual extends (A1 =!= A2 =>> O) {

    def apply(in: A1 =!= A2): O = selectSafer(in.a1, in.a2)
  }
  type ForEqual = ForEqual.type
}

trait UnknownDomain_Imp0 {

  case class D2[
      A1 <: Arity,
      A2 <: Arity,
      O <: Proof
  ]()(
      implicit
      val bound1: A1 ~~> Unknown,
      val bound2: A2 =>> O
  ) extends UnknownDomain[A1, A2, O] {

    override def selectSafer(a1: A1, a2: A2): O = bound2(a2)
  }

  implicit def d2[
      A1 <: Arity,
      A2 <: Arity,
      O <: Proof
  ](
      implicit
      bound1: A1 ~~> Unknown,
      bound2: A2 =>> O
  ): UnknownDomain[A1, A2, O] = D2()
}

object UnknownDomain extends UnknownDomain_Imp0 {

  def summon[
      A1 <: Arity,
      A2 <: Arity,
      O <: Proof
  ](
      implicit
      self: UnknownDomain[A1, A2, O]
  ): UnknownDomain[A1, A2, O] = self

  case class D1[
      A1 <: Arity,
      A2 <: Arity,
      O <: Proof
  ]()(
      implicit
      val bound1: A1 =>> O,
      val bound2: A2 ~~> Unknown
  ) extends UnknownDomain[A1, A2, O] {

    override def selectSafer(a1: A1, a2: A2): O = bound1(a1)
  }

  implicit def d1[
      A1 <: Arity,
      A2 <: Arity,
      O <: Proof
  ](
      implicit
      bound1: A1 =>> O,
      bound2: A2 ~~> Unknown
  ): UnknownDomain[A1, A2, O] = D1()
}
