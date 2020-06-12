package edu.umontreal.kotlingrad.shapesafe.arity

import edu.umontreal.kotlingrad.shapesafe.arity.ArityOps.{CanSum, Proof_==}
import singleton.ops.{+, ==, Require}

object ArityOpsImpl {

  abstract class ??[A1 <: Arity, A2 <: Arity] extends {

    def Equal: Proof_==[A1, A2]

    case object Sum extends CanSum[A1, A2] {

      final type Out = Arity.FromInt_Unsafe
      override def out(a1: A1, a2: A2): Out =
        Arity.FromInt_Unsafe(a1.number + a2.number)
    }
  }

  trait `??_Implicits0` {

    implicit def _2[A1 <: Arity.Unsafe, A2 <: Arity]: ??[A1, A2] = new ??[A1, A2] {

      override case object Equal extends Proof_==[A1, A2] {

        override type Out = A2
        override def _out(a1: A1, a2: A2): Out = a2
      }
    }
  }

  object ?? extends `??_Implicits0` {

    implicit def _1[A1 <: Arity, A2 <: Arity.Unsafe]: ??[A1, A2] = new ??[A1, A2] {

      override case object Equal extends Proof_==[A1, A2] {

        override type Yield = A1
        override def _yieldRT(a1: A1, a2: A2): Yield = a1
      }
    }

  }

  case class Binary[N1, N2]() {

    type A1 = Arity.Constant[N1]
    type A2 = Arity.Constant[N2]

    case class Equal()(implicit lemma: Require[N1 == N2]) extends Proof_==[A1, A2] {

      type Out = A1
      override def _out(a1: A1, a2: A2): A1 = {

        a1
      }
    }

    case class Sum() extends CanSum[A1, A2] {

      type Out = Arity.Constant[N1 + N2]
      override def out(a1: A1, a2: A2): Out = Arity.FromWitness[N1 + N2](a1.number + a2.number)
    }
  }

}
