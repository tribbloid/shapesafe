package org.shapesafe.core.logic

trait HasTactic extends HasTheory {

  import theory._

  // similar to https://leanprover-community.github.io/extras/conv.html
  trait Tactic[I, SUBG, OG] {

    import Tactic._

    type SubGoal = SUBG

    def toGoal[O]: Tactic[I, SUBG, O]

    def cite[O](lemma: SUBG |- O): Partial[I, O, OG]

    def >>[O](lemma: SUBG |- O): Partial[I, O, OG] = {

      cite(lemma)
    }
  }

  object Tactic {

    case class Empty[I, OG](
    ) extends Tactic[I, I, OG] {

      override def toGoal[O]: Empty[I, O] = new Empty[I, O]()

      override def cite[O](lemma: I |- O): Partial[I, O, OG] = {

        Partial[I, O, OG](lemma)
      }
    }

    case class Partial[I, SUBG, OG](
        antecedent: I |- SUBG
    ) extends Tactic[I, SUBG, OG] {

      def toGoal[O]: Partial[I, SUBG, O] = {
        Partial[I, SUBG, O](antecedent)
      }

      def cite[O](lemma: SUBG |- O): Partial[I, O, OG] = {

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
