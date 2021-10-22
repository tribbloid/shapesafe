package org.shapesafe.core

// TODO: DO NOT USE! A |- B is a diverging branch from A |- C
@Deprecated
trait NaturalDeductionMixin {
  self: AxiomSet =>

  implicit def chain[
      A,
      B <: OUB,
      C <: OUB
  ](
      implicit
      lemma1: A |- B,
      lemma2: B |- C
  ): A |- C = Proof.Chain(lemma1, lemma2)
}
