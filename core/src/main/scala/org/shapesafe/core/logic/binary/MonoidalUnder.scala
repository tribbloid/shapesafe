package org.shapesafe.core.logic.binary

trait MonoidalUnder[D, ??[A <: D, B <: D] <: D, Eye <: D] extends Op2Under[D, ??] {

  import theory._

  implicit def eyeL[A <: D]: Theorem[A ?? Eye |- A] = forAll[A ?? Eye].=>> { v =>
    deconstruct(v)._1
  }

  implicit def eyeR[A <: D]: Theorem[Eye ?? A |- A] = forAll[Eye ?? A].=>> { v =>
    deconstruct(v)._2
  }

  implicit def associativeL[
      A <: D,
      B <: D,
      C <: D
  ]: Theorem[A ?? B ?? C |- (A ?? (B ?? C))] = forAll[(A ?? B) ?? C].=>> { v =>
    val (l, r) = deconstruct(v)
    val (ll, lr) = deconstruct(l)

    construct(ll, construct(lr, r))
  }

  implicit def associativeR[
      A <: D,
      B <: D,
      C <: D
  ]: Theorem[A ?? (B ?? C) |- (A ?? B ?? C)] = forAll[A ?? (B ?? C)].=>> { v =>
    val (l, r) = deconstruct(v)
    val (rl, rr) = deconstruct(r)

    construct(construct(l, rl), rr)
  }
}
