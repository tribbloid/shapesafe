package edu.umontreal.kotlingrad.shapesafe.util

import edu.umontreal.kotlingrad.shapesafe.util.ArityOpsImpl.??
import singleton.ops.{==, Require}

object ArityOps {

  trait MayEqual[-A1 <: Arity, -A2 <: Arity] {

    type Yield <: Arity
    final def yieldRT(a1: A1, a2: A2): Yield = {

      require(a1.number == a2.number)
      _yieldRT(a1, a2)
    }

    def _yieldRT(a1: A1, a2: A2): Yield
  }

  object MayEqual {

    implicit def safe[N1, N2](implicit lemma: Require[N1 == N2]): ArityOpsImpl.Binary[N1, N2]#Equal =
      new ArityOpsImpl.Binary[N1, N2]().Equal()

    implicit def unsafe[A1 <: Arity, A2 <: Arity](implicit lemma: A1 ?? A2): MayEqual[A1, A2] = lemma.Equal
  }

  trait CanSum[-A1 <: Arity, -A2 <: Arity] {

    type Yield <: Arity
    def yieldRT(a1: A1, a2: A2): Yield
  }

  object CanSum {

    implicit def safe[N1, N2]: ArityOpsImpl.Binary[N1, N2]#Sum = ArityOpsImpl.Binary[N1, N2]().Sum()

    implicit def unsafe[A1 <: Arity, A2 <: Arity](implicit lemma: A1 ?? A2): CanSum[A1, A2] = lemma.Sum
  }
}
