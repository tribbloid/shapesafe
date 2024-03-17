package shapesafe.core.logic.binary

trait RingAxioms[D, :+[A <: D, B <: D] <: D, :*[A <: D, B <: D] <: D, _0 <: D, _1 <: D] extends GroupAxioms[D, :+, _0] {

  trait Ring_* extends MonoidalUnder[D, :*, _1] {

    final override val theory: RingAxioms.this.theory.type = RingAxioms.this.theory

    val group_+ : Group_+

    import theory._

    implicit def distributiveL[
        A <: D,
        B <: D,
        C <: D
    ]: theory.Theorem[A :* (B :+ C) |- (A :* B :+ (A :* C))] = forAll[A :* (B :+ C)].=>> { v =>
      val (l, r) = deconstruct(v)
      val (rl, rr) = group_+.deconstruct(r)

      group_+.construct(
        construct(l, rl),
        construct(l, rr)
      )
    }

    implicit def distributiveR[
        A <: D,
        B <: D,
        C <: D
    ]: theory.Theorem[A :+ B :* C |- (A :* C :+ (B :* C))] = forAll[(A :+ B) :* C].=>> { v =>
      val (l, r) = deconstruct(v)
      val (ll, lr) = group_+.deconstruct(l)

      group_+.construct(
        construct(ll, r),
        construct(lr, r)
      )
    }
  }

  trait CommutativeRing_* extends Ring_* with AbelianUnder[D, :*, _1] {}
}
