package org.shapesafe.core.logic.binary

import org.shapesafe.core.logic.Theory

trait MonoidalUnder[??[A, B], Eye] extends Op2Under[??] {

  val prove: Theory

  import prove._

  def construct[A, B](a: A, b: B): A ?? B

  def deconstruct[A, B](v: A ?? B): (A, B)

  implicit def substitute[
      A,
      B,
      AO,
      BO
  ](
      implicit
      lemma1: A |- AO,
      lemma2: B |- BO
  ): Axiom[A ?? B |- (AO ?? BO)] = forAll[??[A, B]].=>> { v =>
    val (ll, rr) = deconstruct(v)
    construct(lemma1.instanceFor(ll), lemma2.instanceFor(rr))
  }

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

  implicit def eyeL[A]: Axiom[A ?? Eye |- A] = forAll[A ?? Eye].=>> { v =>
    deconstruct(v)._1
  }

  implicit def eyeR[A]: Axiom[Eye ?? A |- A] = forAll[Eye ?? A].=>> { v =>
    deconstruct(v)._2
  }

  implicit def associativeL[
      A,
      B,
      C
  ]: Axiom[A ?? B ?? C |- (A ?? (B ?? C))] = forAll[(A ?? B) ?? C].=>> { v =>
    val (l, r) = deconstruct(v)
    val (ll, lr) = deconstruct(l)

    construct(ll, construct(lr, r))
  }

  implicit def associativeR[
      A,
      B,
      C
  ]: Axiom[A ?? (B ?? C) |- (A ?? B ?? C)] = forAll[A ?? (B ?? C)].=>> { v =>
    val (l, r) = deconstruct(v)
    val (rl, rr) = deconstruct(r)

    construct(construct(l, rl), rr)
  }
}
