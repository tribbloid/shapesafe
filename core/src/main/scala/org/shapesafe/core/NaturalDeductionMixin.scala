package org.shapesafe.core

trait NaturalDeductionMixin {
  self: ProofScope =>

  implicit def chain[
      A,
      B <: OUB,
      C <: OUB
  ](
      implicit
      lemma1: A |-< B,
      lemma2: B |-< C
  ): A |- C = Proof.Chain(lemma1, lemma2)
}
