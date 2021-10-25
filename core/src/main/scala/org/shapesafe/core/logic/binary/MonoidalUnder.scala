package org.shapesafe.core.logic.binary

trait MonoidalUnder[D, ??[A <: D, B <: D] <: D, Eye <: D] extends Op2Under[D, ??] {

  import theory._

//    implicit def composite1[
//        A ,
//        B ,
//        AO
//    ](
//        implicit
//        lemma1: A |- AO
//    ) = forAll[??[A, B]].=>> { v =>
//      apply(lemma1.valueOf(v.a), v.b)
//    }
//
//    implicit def composite2[
//        A ,
//        B ,
//        BO
//    ](
//        implicit
//        lemma2: B |- BO
//    ) = forAll[??[A, B]].=>> { v =>
//      apply(v.a, lemma2.valueOf(v.b))
//    }

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
