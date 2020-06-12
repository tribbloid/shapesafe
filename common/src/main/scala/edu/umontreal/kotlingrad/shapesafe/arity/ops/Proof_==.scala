package edu.umontreal.kotlingrad.shapesafe.arity.ops

import edu.umontreal.kotlingrad.shapesafe.arity.ArityOpsImpl.??
import edu.umontreal.kotlingrad.shapesafe.arity.{Arity, ArityLike, ArityOp}
import singleton.ops.{==, Require}

abstract class Proof_==[-A1 <: ArityLike, -A2 <: ArityLike]() extends ArityOp {

//    type Out <: Arity
//    final def out(a1: A1, a2: A2): Out = {
//
//      require(a1.number == a2.number)
//      _out(a1, a2)
//    }
//
//    def _out(a1: A1, a2: A2): Out
}

object Proof_== {

  case class Unsafe[A1 <: Arity, A2 <: Arity]()(implicit a1: A1, a2: A2) extends Proof_==[A1, A2]() {

    override val out: Arity = Arity.Un
  }

  implicit def safe[N1, N2](implicit lemma: Require[N1 == N2]): ArityOpsImpl.Binary[N1, N2]#Equal =
    new ArityOpsImpl.Binary[N1, N2]().Equal()

  implicit def unsafe[A1 <: Arity, A2 <: Arity](implicit lemma: A1 ?? A2): Proof_==[A1, A2] = lemma.Equal
}
