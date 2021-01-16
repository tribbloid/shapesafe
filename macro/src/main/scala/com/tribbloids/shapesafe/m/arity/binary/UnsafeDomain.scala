package com.tribbloids.shapesafe.m.arity.binary

import com.tribbloids.shapesafe.m.arity.Utils.Op
import com.tribbloids.shapesafe.m.arity.{Arity, Expression}
import com.tribbloids.shapesafe.m.arity.OfArity._
import singleton.ops.impl.std

import scala.language.{existentials, higherKinds}
import singleton.twoface.impl.TwoFaceAny

abstract class UnsafeDomain[
    A1 <: Expression,
    A2 <: Expression,
    O <: Proof
] {

  def bound1: A1 ~~> Proof
  def bound2: A2 ~~> Proof

  def selectSafer(a1: A1, a2: A2): O

  case class ForOp2[??[X1, X2] <: Op]()(
      implicit tfs: TwoFaceAny.Int.Shell2[??, Int, std.Int, Int, std.Int]
  ) extends (Expr2[A1, A2, ??] ForAll Unsafe) {

    case class proveArity(in: Expr2[A1, A2, ??]) extends Unsafe {

      override def out: Arity.Unsafe = {

        val n1 = bound1.apply(in.a1).out.numberOpt
        val n2 = bound2.apply(in.a2).out.numberOpt

        (n1, n2) match {

          case (Some(_1), Some(_2)) =>
            val nOut = tfs.apply(n1.get, n2.get).getValue
            Arity.Dynamic(nOut)
          case _ =>
            Arity.Unknown
        }

      }
    }
  }

  case object ForEqual extends (AssertEqual[A1, A2] ~~> Proof) {

    def apply(in: AssertEqual[A1, A2]): Proof = selectSafer(in.a1, in.a2)
  }
  type ForEqual = ForEqual.type
}

trait UnsafeDomain_Imp0 {

  case class D2[
      A1 <: Expression,
      A2 <: Expression,
      O <: Proof
  ]()(
      implicit
      val bound1: A1 ~~> Unsafe,
      val bound2: A2 ~~> O
  ) extends UnsafeDomain[A1, A2, O] {

    override def selectSafer(a1: A1, a2: A2): O = bound2(a2)
  }

  implicit def d2[
      A1 <: Expression,
      A2 <: Expression,
      O <: Proof
  ](
      implicit
      bound1: A1 ~~> Unsafe,
      bound2: A2 ~~> O
  ): UnsafeDomain[A1, A2, O] = D2()
}

object UnsafeDomain extends UnsafeDomain_Imp0 {

  def summon[
      A1 <: Expression,
      A2 <: Expression,
      O <: Proof
  ](
      implicit
      self: UnsafeDomain[A1, A2, O]
  ): UnsafeDomain[A1, A2, O] = self

  case class D1[
      A1 <: Expression,
      A2 <: Expression,
      O <: Proof
  ]()(
      implicit
      val bound1: A1 ~~> O,
      val bound2: A2 ~~> Unsafe
  ) extends UnsafeDomain[A1, A2, O] {

    override def selectSafer(a1: A1, a2: A2): O = bound1(a1)
  }

  implicit def d1[
      A1 <: Expression,
      A2 <: Expression,
      O <: Proof
  ](
      implicit
      bound1: A1 ~~> O,
      bound2: A2 ~~> Unsafe
  ): UnsafeDomain[A1, A2, O] = D1()
}
