package org.shapesafe.core.logic.binary

import org.shapesafe.core.logic.HasTheory

trait RingAxioms[D, :+[A <: D, B <: D] <: D, :*[A <: D, B <: D] <: D, _0 <: D, _1 <: D] extends HasTheory {

  trait Ring_+ extends AbelianUnder[D, :+, _0] {

    final override val theory: RingAxioms.this.theory.type = RingAxioms.this.theory
  }

  trait Ring_* extends MonoidalUnder[D, :*, _1] {

    final override val theory: RingAxioms.this.theory.type = RingAxioms.this.theory

    val ring_+ : Ring_+

    import theory._

    implicit def distributiveL[
        A <: D,
        B <: D,
        C <: D
    ] = forAll[A :* (B :+ C)].=>> { v =>
      val (l, r) = deconstruct(v)
      val (rl, rr) = ring_+.deconstruct(r)

      ring_+.construct(
        construct(l, rl),
        construct(l, rr)
      )
    }

    implicit def distributiveR[
        A <: D,
        B <: D,
        C <: D
    ] = forAll[(A :+ B) :* C].=>> { v =>
      val (l, r) = deconstruct(v)
      val (ll, lr) = ring_+.deconstruct(l)

      ring_+.construct(
        construct(ll, r),
        construct(lr, r)
      )
    }
  }

  trait CommutativeRing_* extends Ring_* with AbelianUnder[D, :*, _1] {}
}
