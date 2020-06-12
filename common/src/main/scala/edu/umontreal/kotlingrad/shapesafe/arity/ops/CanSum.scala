package edu.umontreal.kotlingrad.shapesafe.arity.ops

import edu.umontreal.kotlingrad.shapesafe.arity.{ArityLike, ArityOp}

abstract class CanSum[-A1 <: ArityLike, -A2 <: ArityLike](a1: A1, a2: A2) extends ArityOp {

//    type Out <: Arity
//    def out(a1: A1, a2: A2): Out
}

object CanSum {

  implicit def safe[N1, N2]: ArityOpsImpl.Binary[N1, N2]#Sum = ArityOpsImpl.Binary[N1, N2]().Sum()

  implicit def unsafe[A1 <: Arity, A2 <: Arity](implicit lemma: A1 ?? A2): CanSum[A1, A2] = lemma.Sum
}
