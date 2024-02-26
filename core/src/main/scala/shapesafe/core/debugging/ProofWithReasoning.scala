package shapesafe.core.debugging

import shapesafe.core.logic.ProofSystem

/**
  * White box [[ProofSystem]], can prove things and explain it (or the reason not being able to do so)
  */
trait ProofWithReasoning extends ProofSystem with Reporters {

  trait Reasoner {

    type Goal <: HasNotation

    object Peek extends PeekReporter[HasNotation, Goal]
    type Peek[I <: HasNotation] = Peek.CaseFrom[I]

    object Interrupt extends InterruptReporter[HasNotation, Goal]
    type Interrupt[I <: HasNotation] = Interrupt.CaseFrom[I]

    trait ReasoningMixin {
      self: Proof[_, _] =>
    }

    type |-@-[I <: HasNotation, O <: Goal] = (I |- O) with ReasoningMixin

    object ReasoningMixin {

      implicit def reason[I <: HasNotation, P <: Consequent](
          implicit
          reporter: Peek.CaseFrom[I],
          prove: Proof[I, P]
      ): Proof[I, P] with ReasoningMixin = {

        prove.asInstanceOf[Proof[I, P] with ReasoningMixin]
      }
    }

  }
}
