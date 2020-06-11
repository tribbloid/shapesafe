package edu.umontreal.kotlingrad.shapesafe.arity.proof

import edu.umontreal.kotlingrad.shapesafe.arity.ops.{??, BinaryOps}
import edu.umontreal.kotlingrad.shapesafe.arity.{Arity, ArityOp}
import singleton.ops.{==, Require}

trait MayEqual[-A1 <: Arity, -A2 <: Arity] extends ArityOp.BinaryOp[A1, A2] {

  final def out(a1: A1, a2: A2): Out = {

    require(a1.number == a2.number)
    _out(a1, a2)
  }

  def _out(a1: A1, a2: A2): Out
}

object MayEqual {

  implicit def const[N1, N2](implicit lemma: Require[N1 == N2]): BinaryOps.Const[N1, N2]#Equal =
    BinaryOps.Const[N1, N2]().Equal()

  implicit def unsafe[A1 <: Arity, A2 <: Arity](implicit lemma: A1 ?? A2): MayEqual[A1, A2] =
    lemma.ProbablyEqual

  // TODO: how to add rule of commutativity, associativity etc. for symbol Arity in the future?
}
