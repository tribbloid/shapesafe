package edu.umontreal.kotlingrad.shapesafe.`macro`.arity.binary

import edu.umontreal.kotlingrad.shapesafe.`macro`.arity.Utils.Op
import edu.umontreal.kotlingrad.shapesafe.`macro`.arity.{Arity, Expr, Proof}

import scala.language.higherKinds

trait UnsafeDomain[A1 <: Proof, A2 <: Proof] {

  def ProbablyEqual: A1 MayEqual A2

  class Op2Impl[Fr[X, Y] <: Op]() extends Expr.Out_=[Arity.Unknown.type] with Op2[A1, A2, Fr] {}
}

object UnsafeDomain extends UnsafeDomain_Implicits0 {

  class _1[A1 <: Proof, A2 <: Proof.Unsafe](val a1: A1) extends UnsafeDomain[A1, A2] {

    override object ProbablyEqual extends (A1 MayEqual A2) {

      override type Out = a1.Out
      override val out: Out = a1.out
    }
  }

  implicit def summon1[A1 <: Proof, A2 <: Proof.Unsafe](implicit a1: A1): _1[A1, A2] =
    new _1[A1, A2](a1)
}
