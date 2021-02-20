//package org.shapesafe.core
//
//import org.shapesafe.BaseSpec
//
//class ProofSystemSpike1 extends BaseSpec {}
//
//object ProofSystemSpike1 {
//
//  trait Boolean
//  trait True extends Boolean
//
//  object Sys extends ProofSystem[Boolean]
//  import Sys._
//
//  trait NOT[-K]
//  implicit def !![S]: NOT[NOT[S]] =>> S = ???
//
//  trait Number
//
//  trait Rational extends Number
//  object Two extends Rational
//  type Two = Two.type
//
//  trait Irrational extends Number with NOT[Rational]
//  object SqrtTwo extends Irrational
//  type SqrtTwo = SqrtTwo.type
//
//  trait ^^[A, B]
//  trait **[A, B]
//
//  implicit def commutative[A, B]: (A ** B) =>> (B ** A) = ???
//
//  implicit def means[A]: (A ^^ Two) =>> (A ** A) = ???
//
//  implicit def chain[A, B, C]: ((A ^^ B) ^^ C) =>> (A ^^ (B ** C)) = ???
//  implicit def tt: (SqrtTwo ** SqrtTwo) =>> Two = ???
//
//  implicit def r1[A, B, C]: ((A ** B) =:= (A ** C)) =>> B =:= C = ???
//  implicit def r2[A, B, C]: ((A ^^ B) =:= (A ^^ C)) =>> B =:= C = ???
//
//  type V = (SqrtTwo ^^ SqrtTwo) ^^ SqrtTwo
//
//  // becomes
//  type VV = (SqrtTwo ^^ Two)
//
//  implicitly[(SqrtTwo ^^ SqrtTwo) ^^ SqrtTwo ~~> Number](
//    from[SqrtTwo ^^ SqrtTwo ^^ SqrtTwo].=>> { v =>
//      val c = chain[SqrtTwo, SqrtTwo, SqrtTwo]
//      c.valueOf(v)
//    }
//  )
//
//  //...........
//  //...........
//
//  //target
////  implicitly[(SqrtTwo ^^ SqrtTwo) ~~> Rational]
//
//}
