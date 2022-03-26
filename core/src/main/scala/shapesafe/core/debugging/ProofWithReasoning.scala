package shapesafe.core.debugging

import shapesafe.core.logic.ProofSystem

/**
  * White box [[ProofSystem]], can prove things and explain it (or the reason not being able to do so)
  */
trait ProofWithReasoning extends ProofSystem with Reporters {

  trait Reasoner {

    type Goal <: CanPeek

    object Peek extends PeekReporter[CanPeek, Goal]
    type Peek[I <: CanPeek] = Peek.Case[I]

    object Interrupt extends InterruptReporter[CanPeek, Goal]
    type Interrupt[I <: CanPeek] = Interrupt.Case[I]

    trait ReasoningMixin {
      self: Proof[_, _] =>
    }

    type |-@-[I <: CanPeek, O <: Goal] = (I |- O) with ReasoningMixin

    object ReasoningMixin {

      implicit def reason[I <: CanPeek, P <: Consequent](
          implicit
          reporter: Peek.Case[I],
          prove: Proof[I, P]
      ): Proof[I, P] with ReasoningMixin = {

        prove.asInstanceOf[Proof[I, P] with ReasoningMixin]
      }
    }

  }
}
