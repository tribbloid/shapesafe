package org.shapesafe.core

trait HasTactic {
  self: ProofScope =>

  // similar to https://leanprover-community.github.io/extras/conv.html
  trait Tactic[I, SUBG, OG <: OUB] {

    import Tactic._

    type SubGoal = SUBG

    def toGoal[O <: OUB]: Tactic[I, SUBG, O]

    def cite[O <: OG](lemma: SUBG |- O): Partial[I, O, OG]

    def >>[O <: OG](lemma: SUBG |- O): Partial[I, O, OG] = {

      cite(lemma)
    }
  }

  object Tactic {

    case class Empty[I, OG <: OUB](
    ) extends Tactic[I, I, OG] {

      override def toGoal[O <: OUB]: Empty[I, O] = new Empty[I, O]()

      override def cite[O <: OG](lemma: I |- O): Partial[I, O, OG] = {

        Partial[I, O, OG](lemma)
      }
    }

    case class Partial[I, SUBG <: OUB, OG <: OUB](
        antecedent: I |- SUBG
    ) extends Tactic[I, SUBG, OG] {

      def toGoal[O <: OUB]: Partial[I, SUBG, O] = {
        Partial[I, SUBG, O](antecedent)
      }

      def cite[O <: OUB](lemma: SUBG |- O): Partial[I, O, OG] = {

        Partial(
          Proof.Chain[I, SUBG, O](antecedent, lemma)
        )
      }

      def fulfil(
          implicit
          canUpcast: SUBG <:< OG
      ): I |- SUBG = antecedent.asInstanceOf[I |- SUBG]

      def complement[SUBG2 <: OG](
          implicit
          trivialLemma: SUBG |- SUBG2
      ): I |- SUBG2 = {
        cite(trivialLemma).fulfil
      }
    }
  }
}
