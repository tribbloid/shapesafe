package edu.umontreal.kotlingrad.shapesafe.util

import edu.umontreal.kotlingrad.shapesafe.util.ArityOps.{CanSum, MayEqual}
import singleton.ops.{+, ==, Require}

object ArityOpsImpl {

  abstract class ??[A1 <: Arity, A2 <: Arity] extends {

    def Equal: MayEqual[A1, A2]

    case object Sum extends CanSum[A1, A2] {

      final type Yield = Arity.OfInt_Unsafe
      override def yieldRT(a1: A1, a2: A2): Yield =
        Arity.OfInt_Unsafe(a1.number + a2.number)
    }
  }

  trait `??_Implicits0` {

    implicit def _2[A1 <: Arity.Unsafe, A2 <: Arity]: ??[A1, A2] = new ??[A1, A2] {

      override case object Equal extends MayEqual[A1, A2] {

        override type Yield = A2
        override def _yieldRT(a1: A1, a2: A2): Yield = a2
      }
    }
  }

  object ?? extends `??_Implicits0` {

    implicit def _1[A1 <: Arity, A2 <: Arity.Unsafe]: ??[A1, A2] = new ??[A1, A2] {

      override case object Equal extends MayEqual[A1, A2] {

        override type Yield = A1
        override def _yieldRT(a1: A1, a2: A2): Yield = a1
      }
    }

  }

  case class Binary[N1, N2]() {

    type A1 = Arity.Constant[N1]
    type A2 = Arity.Constant[N2]

    case class Equal()(implicit lemma: Require[N1 == N2]) extends MayEqual[A1, A2] {

      type Yield = A1
      override def _yieldRT(a1: A1, a2: A2): A1 = {

        a1
      }
    }

    case class Sum() extends CanSum[A1, A2] {

      type Yield = Arity.Constant[N1 + N2]
      override def yieldRT(a1: A1, a2: A2): Yield = Arity.OfInt[N1 + N2](a1.number + a2.number)
    }
  }

}
