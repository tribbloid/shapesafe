package edu.umontreal.kotlingrad.shapesafe.util

import edu.umontreal.kotlingrad.shapesafe.util.ArityOpsImpl.??
import shapeless.ops.hlist
import shapeless.ops.nat.ToInt
import shapeless.{HList, Nat}
import singleton.ops.{==, Require}

object ArityOps {

  // TODO: technically this is not an ops, but a proof
  case class OfSize[Data <: HList, N <: Nat](number: Int) {

    type _N = singleton.ops.ToInt[N] // getting rid of church encoding

    type Yield = Arity.FromSize[_N]

    lazy val yieldRT: Yield = Arity.FromSize[_N](number)
  }

  object OfSize {

    implicit def observe[Data <: HList, T <: Nat](
        implicit
        getSize: hlist.Length.Aux[Data, T],
        toInt: ToInt[T]
    ): OfSize[Data, T] = {

      new OfSize[Data, T](Nat.toInt[T])
    }
  }

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
