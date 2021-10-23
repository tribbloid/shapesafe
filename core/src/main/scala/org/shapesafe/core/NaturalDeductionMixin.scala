package org.shapesafe.core

trait NaturalDeductionMixin_Imp0 {
  self: AxiomSet =>

  implicit def hypotheticalSyllogism_backward[
      A,
      B <: OUB,
      C <: OUB
  ](
      implicit
      lemma2: Axiom[B |- C],
      lemma1: A |- B
  ): A |- C = Proof.Chain(lemma1, lemma2)
}

trait NaturalDeductionMixin extends NaturalDeductionMixin_Imp0 {
  self: AxiomSet =>

  implicit def hypotheticalSyllogism_forward[
      A,
      B <: OUB,
      C <: OUB
  ](
      implicit
      lemma1: Axiom[A |- B],
      lemma2: B |- C
  ): A |- C = Proof.Chain(lemma1, lemma2)
}
