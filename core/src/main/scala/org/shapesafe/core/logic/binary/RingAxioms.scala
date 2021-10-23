package org.shapesafe.core.logic.binary

class RingAxioms[:+[A, B], :*[A, B], _0, _1] {

  trait Ring_+ extends AbelianUnder[:+, _0] {}

  trait Ring_* extends MonoidalUnder[:+, _1] {

    val ring_+ : Ring_+

    import prove._

    implicit def distributiveL[
        A,
        B,
        C
    ] = forAll[A :* (B :+ C)].=>> { v =>
      val (l, r) = deconstruct(v)
      val (rl, rr) = ring_+.deconstruct(r)

      ring_+.construct(
        construct(l, rl),
        construct(l, rr)
      )
    }

    implicit def distributiveR[
        A,
        B,
        C
    ] = forAll[(A :+ B) :* C].=>> { v =>
      val (l, r) = deconstruct(v)
      val (ll, lr) = ring_+.deconstruct(l)

      ring_+.construct(
        construct(ll, r),
        construct(lr, r)
      )
    }
  }

  trait CommutativeRing_* extends Ring_* with AbelianUnder[:*, _1] {}
}
