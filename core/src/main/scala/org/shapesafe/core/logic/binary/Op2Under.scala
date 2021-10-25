package org.shapesafe.core.logic.binary

import org.shapesafe.core.logic.NaturalDeduction

object Op2Under {

  trait _Imp0[D, ??[A <: D, B <: D]] extends NaturalDeduction {
    import theory._

    def construct[A <: D, B <: D](a: A, b: B): A ?? B

    def deconstruct[A <: D, B <: D](v: A ?? B): (A, B)

    implicit def substituteR[
        A <: D,
        B <: D,
        BO <: D
    ](
        implicit
        lemma2: B |- BO
    ): Theorem[A ?? B |- (A ?? BO)] = forAll[??[A, B]].=>> { v =>
      val (ll, rr) = deconstruct(v)
      construct(ll, lemma2.instanceFor(rr))
    }
  }
}

trait Op2Under[D, ??[A <: D, B <: D]] extends Op2Under._Imp0[D, ??] {
  import theory._

  implicit def substituteL[
      A <: D,
      B <: D,
      AO <: D
  ](
      implicit
      lemma1: A |- AO
  ): Theorem[A ?? B |- (AO ?? B)] = forAll[??[A, B]].=>> { v =>
    val (ll, rr) = deconstruct(v)
    construct(lemma1.instanceFor(ll), rr)
  }
}
