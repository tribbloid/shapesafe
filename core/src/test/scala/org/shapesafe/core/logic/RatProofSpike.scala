package org.shapesafe.core.logic

import org.shapesafe.Spike

object RatProofSpike {

  object Sys extends ProofSystem

  import Sys._

  trait Lemma1Like {
    // objective: prove that (I1 * I1 == 2 * I2) |- \exist I3 such that I2 = 2 * I3

    trait E // expression

    trait R extends E // these can be mixed into expression at will
    trait Q extends R
    trait I extends Q
    object `2` extends I
    type `2` = `2`.type

    trait ==[A <: E, B <: E] extends E

    trait :*[A <: E, B <: E] extends E
    trait :/[A <: E, B <: E] extends E

    trait Union[A <: E, B <: E] extends E

    implicit def move[
        A <: E,
        B <: E,
        C <: E
    ]: (A == B :* C) |- (A :/ C == B) = {
      ???
    }

    implicit def selfContained1_*[
        A <: I,
        B <: E,
        C <: E
    ](
        implicit
        bi: B with I
    ): (A == B :* C) |- B with I = ???

    implicit def selfContained2_*[
        A <: I,
        B <: E,
        C <: E
    ](
        implicit
        bi: C with I
    ): (A == B :* C) |- C with I = ???
  }

//  implicit def smallestRatio[N <: R, A <: R, B <: R](
//      implicit
////      a: A,
////      b: B,
//      aP: IsRational[A],
//      bP: IsRational[B]
//  ): IsRational[N] |- N == (A :/ B) = {
//    ???
//  }

//  object Sqrt2 extends R
//  type Sqrt2 = Sqrt2.type

//  implicit lazy val sqrt2Defined: (Sqrt2 :* Sqrt2) == `2` = ???

  {
    // by contradiction
//    implicit def fake: IsRational[Sqrt2] = ???

    //objective:

    /**
      * Sqrt2
      * Sqrt2 == (A :/ B)
      * (Sqrt2 :* Sqrt2) == (A :/ B) :* (A :/ B)
      *    == (A :* A) :/ (B :* B) == `2`
      * (A :* A) == `2` :* (B :* B)
      *
      * lemma: if A <: I, B <: I, A * A == 2 * B, then exist C <: I, where B * B == 2 * C * C
      *
      * (A / 2) * A = B
      * (A / 2) * (A / 2) = B / 2
      */
  }

//  trait Rational extends R
//  object Two extends Rational
//  type Two = Two.type
//
//  trait Irrational extends R with NOT[Rational]
//  object SqrtTwo extends Irrational
//  type SqrtTwo = SqrtTwo.type
//
//  trait ^^[A, B]
//  trait **[A, B]

//  implicit def commutative[A, B]: (A :* B) =>> (B :* A) = ???
//
//  implicit def means[A]: (A ^^ Two) =>> (A :* A) = ???
//
//  implicit def chain[A, B, C]: ((A ^^ B) ^^ C) =>> (A ^^ (B :* C)) = ???
//  implicit def tt: (SqrtTwo :* SqrtTwo) =>> Two = ???
//
//  implicit def r1[A, B, C]: ((A :* B) =:= (A :* C)) =>> B =:= C = ???
//  implicit def r2[A, B, C]: ((A ^^ B) =:= (A ^^ C)) =>> B =:= C = ???
//
//  type V = (SqrtTwo ^^ SqrtTwo) ^^ SqrtTwo
//
//  // becomes
//  type VV = (SqrtTwo ^^ Two)
//
//  implicitly[(SqrtTwo ^^ SqrtTwo) ^^ SqrtTwo ~~> R](
//    from[SqrtTwo ^^ SqrtTwo ^^ SqrtTwo].=>> { v =>
//      val c = chain[SqrtTwo, SqrtTwo, SqrtTwo]
//      c.valueOf(v)
//    }
//  )

  //...........
  //...........

  //target
//  implicitly[(SqrtTwo ^^ SqrtTwo) ~~> Rational]

}

class RatProofSpike extends Spike {}
