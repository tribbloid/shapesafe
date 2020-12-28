package com.tribbloids.shapesafe.m.arity.binary

import com.tribbloids.shapesafe.m.arity.Utils.Op
import com.tribbloids.shapesafe.m.arity.{Arity, Expression, ProofOfArity}
import com.tribbloids.shapesafe.m.~~>
import singleton.ops.impl.std

import scala.language.{existentials, higherKinds}
import singleton.twoface.impl.TwoFaceAny

abstract class UnsafeDomain[
    A1 <: Expression,
    A2 <: Expression,
    O <: ProofOfArity
] {

  def bound1: A1 ~~> ProofOfArity
  def bound2: A2 ~~> ProofOfArity

  def selectSafer(a1: A1, a2: A2): O

  case class ProbablyEqual(
      self: MayEqual[A1, A2]
  ) extends ProofOfArity {

    // TODO: if this type is not narrow enough, introduce a new generic param to outer class
    val proof: O = selectSafer(self.a1, self.a2)

    override type Out = proof.Out

    override def out: Out = proof.out
  }

  case class Op2Proof[??[X1, X2] <: Op](
      self: Expr2[A1, A2, ??]
  )(
      implicit tfs: TwoFaceAny.Int.Shell2[??, Int, std.Int, Int, std.Int] // TODO: kind of too internal
  ) extends ProofOfArity.Unsafe {

    override def out: Arity.Unsafe = {

      val n1 = bound1.apply(self.a1).out.numberOpt
      val n2 = bound2.apply(self.a2).out.numberOpt

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

trait UnsafeDomain_Imp0 {

  case class D2[
      A1 <: Expression,
      A2 <: Expression,
      O <: ProofOfArity
  ]()(
      implicit
      val bound1: A1 ~~> ProofOfArity.UnsafeLike,
      val bound2: A2 ~~> O
  ) extends UnsafeDomain[A1, A2, O] {

    override def selectSafer(a1: A1, a2: A2): O = bound2(a2)
  }

  implicit def d2[
      A1 <: Expression,
      A2 <: Expression,
      O <: ProofOfArity
  ](
      implicit
      bound1: A1 ~~> ProofOfArity.UnsafeLike,
      bound2: A2 ~~> O
  ): UnsafeDomain[A1, A2, O] = D2()
}

object UnsafeDomain extends UnsafeDomain_Imp0 {

  def summon[
      A1 <: Expression,
      A2 <: Expression,
      O <: ProofOfArity
  ](
      implicit
      self: UnsafeDomain[A1, A2, O]
  ): UnsafeDomain[A1, A2, O] = self

  case class D1[
      A1 <: Expression,
      A2 <: Expression,
      O <: ProofOfArity
  ]()(
      implicit
      val bound1: A1 ~~> O,
      val bound2: A2 ~~> ProofOfArity.UnsafeLike
  ) extends UnsafeDomain[A1, A2, O] {

    override def selectSafer(a1: A1, a2: A2): O = bound1(a1)
  }

  implicit def d1[
      A1 <: Expression,
      A2 <: Expression,
      O <: ProofOfArity
  ](
      implicit
      bound1: A1 ~~> O,
      bound2: A2 ~~> ProofOfArity.UnsafeLike
  ): UnsafeDomain[A1, A2, O] = D1()
}
