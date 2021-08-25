package org.shapesafe.core.logic

object NaturalDeductionMixin {
  trait _Imp0 {
    self: Theory =>

    implicit def hypotheticalSyllogism_backward[
        A,
        B <: OUB,
        C <: OUB
    ](
        implicit
        minorPremise: Axiom[B |- C],
        majorPremise: A |- B
    ): A |- C = Proof.Chain(majorPremise, minorPremise)
  }
}

trait NaturalDeductionMixin extends NaturalDeductionMixin._Imp0 {
  self: Theory =>

  implicit def hypotheticalSyllogism_forward[
      A,
      B <: OUB,
      C <: OUB
  ](
      implicit
      majorPremise: Axiom[A |- B],
      minorPremise: B |- C
  ): A |- C = Proof.Chain(majorPremise, minorPremise)
}
