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

  object Prove extends ProofSystem.^[Stuff]

  object Stuff {

    import Prove._

    implicit def id[A <: Stuff]: A |- A = forAll[A].=>>(v => v)
  }

//  trait CompositeUnder[??[A <: Stuff, B <: Stuff] <: Op2[A, B]] {}

  trait MonoidalUnder[??[A <: Stuff, B <: Stuff] <: Op2[A, B], Eye <: Stuff] {

//    import Prove._

    def apply[A <: Stuff, B <: Stuff](a: A, b: B): A ?? B

//    implicit class Composite[
//        A <: Stuff,
//        B <: Stuff,
//        AO <: Stuff,
//        BO <: Stuff
//    ](
//        implicit
//        lemma1: A |- AO,
//        lemma2: B |- BO
//    ) extends (??[A, B] |- (AO ?? BO)) {}

    implicit def reconstruct[
        A <: Stuff,
        B <: Stuff,
        AO <: Stuff,
        BO <: Stuff
    ](
        implicit
        lemma1: A |- AO,
        lemma2: B |- BO
    ) = forAll[??[A, B]].=>> { v =>
      apply(lemma1.instanceFor(v.ll), lemma2.instanceFor(v.rr))
    }

//    implicit def composite1[
//        A <: Stuff,
//        B <: Stuff,
//        AO <: Stuff
//    ](
//        implicit
//        lemma1: A |- AO
//    ) = forAll[??[A, B]].=>> { v =>
//      apply(lemma1.valueOf(v.a), v.b)
//    }
//
//    implicit def composite2[
//        A <: Stuff,
//        B <: Stuff,
//        BO <: Stuff
//    ](
//        implicit
//        lemma2: B |- BO
//    ) = forAll[??[A, B]].=>> { v =>
//      apply(v.a, lemma2.valueOf(v.b))
//    }

    implicit def withEye[A <: Stuff] = Prove.forAll[A ?? Eye].=>> { v =>
      v.ll
    }

    implicit def associative[
        A <: Stuff,
        B <: Stuff,
        C <: Stuff
    ] = Prove.forAll[(A ?? B) ?? C].=>> { v =>
      apply(v.ll.ll, apply(v.ll.rr, v.rr))
    }
  }

  trait AbelianUnder[??[A <: Stuff, B <: Stuff] <: Op2[A, B], Eye <: Stuff] extends MonoidalUnder[??, Eye] {

//    import Prove._

    implicit def commutative[
        A <: Stuff,
        B <: Stuff
    ] = Prove.forAll[A :+ B].=>> { v =>
      apply(v.rr, v.ll)
    }
  }

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
