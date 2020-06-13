package edu.umontreal.kotlingrad.shapesafe.common.arity.binary

import edu.umontreal.kotlingrad.shapesafe.common.arity.{Arity, ArityLike, Expr}

import scala.language.higherKinds

trait Unsafe[A1 <: ArityLike, A2 <: ArityLike] {

  def ProbablyEqual: A1 MayEqual A2

  class ToUnknown[ConstOp[_, _]](
      )
      extends Expr.Out[Arity.Unknown.type]
      with Op2[A1, A2, ConstOp] {}
}

object Unsafe extends Unsafe_Implicits0 {

  class _1[A1 <: ArityLike, A2 <: ArityLike.Unsafe](val a1: A1) extends Unsafe[A1, A2] {

    override object ProbablyEqual extends (A1 MayEqual A2) {

      override type Out = a1.Out
      override val Out: Out = a1.Out
    }
  }

  implicit def summon1[A1 <: ArityLike, A2 <: ArityLike.Unsafe](implicit a1: A1): _1[A1, A2] =
    new _1[A1, A2](a1)
}
