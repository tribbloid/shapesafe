package org.shapesafe.core.logic

object NaturalDeduction {
  trait _Imp0 extends HasTheory {

//    import theory._
//
//    implicit def hypotheticalSyllogism_backward[
//        A,
//        B,
//        C
//    ](
//        implicit
//        minorPremise: Theorem[B |- C],
//        majorPremise: A |- B
//    ): A |- C = Proof.Chain(majorPremise, minorPremise)
  }
}

trait NaturalDeduction extends NaturalDeduction._Imp0 {

  import theory._

  implicit def hypotheticalSyllogism_forward[
      A,
      B,
      C
  ](
      implicit
      majorPremise: Theorem[A |- B],
      minorPremise: B |- C
  ): A |- C = Proof.Chain(majorPremise, minorPremise)
}
