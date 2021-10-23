package org.shapesafe.core.fixtures

import org.shapesafe.core.fixtures.CommutativeRingAxioms.Prove.{|-, forAll}
import org.shapesafe.core.logic.ProofSystem

object CommutativeRingAxioms {

  trait Stuff {

//    type +[T] <: Thing
//    type *[T] <: Thing
  }

  trait Leaf extends Stuff

  object _0 extends Leaf
  object _1 extends Leaf

  trait Op2[L <: Stuff, R <: Stuff] extends Stuff {
    def ll: L
    def rr: R
  }
  trait :+[A <: Stuff, B <: Stuff] extends Op2[A, B]
  trait :*[A <: Stuff, B <: Stuff] extends Op2[A, B]

  type Sq[A <: Stuff] = A :* A

  object Prove extends ProofSystem

  object Stuff {

    import Prove._

    implicit def id[A <: Stuff]: A |- A = forAll[A].=>>(v => v)
  }

//  trait CompositeUnder[??[A <: Stuff, B <: Stuff] <: Op2[A, B]] {}

  object :+ extends AbelianUnder[:+, _0.type] {
    override def apply[A <: Stuff, B <: Stuff](a: A, b: B): A :+ B = ???
  }

  object :* extends AbelianUnder[:*, _1.type] {

    override def apply[A <: Stuff, B <: Stuff](a: A, b: B): A :* B = ???

    implicit def distributiveL[
        A <: Stuff,
        B <: Stuff,
        C <: Stuff
    ] = Prove.forAll[A :* (B :+ C)].=>> { v =>
      :+(:*(v.ll, v.rr.ll), :*(v.ll, v.rr.rr))
    }

    implicit def distributiveR[
        A <: Stuff,
        B <: Stuff,
        C <: Stuff
    ] = Prove.forAll[(A :+ B) :* C].=>> { v =>
      :+(:*(v.ll.ll, v.rr), :*(v.ll.rr, v.rr))
    }
  }

//  object Stuff {
//
////    import Prove._
//
//    implicit def commutative_+[
//        A <: Stuff,
//        B <: Stuff
//    ] = Prove.forAll[A :+ B].=>> { _ =>
//      new :+[B, A] {}
//    }
//
//    implicit def commutative_*[
//        A <: Stuff,
//        B <: Stuff
//    ] = Prove.forAll[A :* B].=>> { _ =>
//      new :*[B, A] {}
//    }
//
//    implicit def associative_+[
//        A <: Stuff,
//        B <: Stuff,
//        C <: Stuff
//    ] = Prove.forAll[(A :+ B) :+ C].=>> { _ =>
//      new (A :+ (B :+ C)) {}
//    }
//
//    implicit def associative_*[
//        A <: Stuff,
//        B <: Stuff,
//        C <: Stuff
//    ] = Prove.forAll[(A :* B) :* C].=>> { _ =>
//      new (A :* (B :* C)) {}
//    }
//  }
}
