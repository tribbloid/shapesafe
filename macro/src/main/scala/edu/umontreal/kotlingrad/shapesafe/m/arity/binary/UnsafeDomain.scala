package edu.umontreal.kotlingrad.shapesafe.m.arity.binary

import edu.umontreal.kotlingrad.shapesafe.m.arity.Utils.Op
import edu.umontreal.kotlingrad.shapesafe.m.arity.{Implies, Operand, Proof}

import scala.language.{existentials, higherKinds}

abstract class UnsafeDomain[
    A1 <: Operand,
    A2 <: Operand
] {

  def selectSafer(a1: A1, a2: A2): Proof

  case class ProbablyEqual(
      self: MayEqual[A1, A2]
  ) extends Proof {

    // TODO: if this type is not narrow enough, introduce a new generic param to outer class
    val proof: Proof = {
      selectSafer(self.a1, self.a2)
    }

    override type Out = proof.Out

    override def out: Out = proof.out
  }

  case class Op2Proof[??[X1, X2] <: Op](
      self: Op2[A1, A2, ??]
  ) extends Proof.Unsafe {}
}

trait UnsafeDomain_Imp0 {

  case class D2[
      A1 <: Operand,
      A2 <: Operand
  ]()(
      implicit
      bound1: A1 Implies Proof.UnsafeLike,
      bound2: A2 Implies Proof
  ) extends UnsafeDomain[A1, A2] {

    override def selectSafer(a1: A1, a2: A2): Proof = bound2(a2)
  }

  implicit def d2[
      A1 <: Operand,
      A2 <: Operand
  ](
      implicit
      bound1: A1 Implies Proof.UnsafeLike,
      bound2: A2 Implies Proof
  ): UnsafeDomain[A1, A2] = D2()
}

object UnsafeDomain extends UnsafeDomain_Imp0 {

  def summon[
      A1 <: Operand,
      A2 <: Operand
  ](
      implicit
      self: UnsafeDomain[A1, A2]
  ): UnsafeDomain[A1, A2] = self

  case class D1[
      A1 <: Operand,
      A2 <: Operand
  ]()(
      implicit
      bound1: A1 Implies Proof,
      bound2: A2 Implies Proof.UnsafeLike
  ) extends UnsafeDomain[A1, A2] {

    override def selectSafer(a1: A1, a2: A2): Proof = bound1(a1)
  }

  implicit def d1[
      A1 <: Operand,
      A2 <: Operand
  ](
      implicit
      bound1: A1 Implies Proof,
      bound2: A2 Implies Proof.UnsafeLike
  ): UnsafeDomain[A1, A2] = D1()
}
