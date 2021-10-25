package org.shapesafe.core.logic.binary

import org.shapesafe.BaseSpec
import org.shapesafe.core.logic.ProofSystem

object RingAxiomsSpec {

  trait DummyRing {

    trait Var
    object _0 extends Var
    object _1 extends Var

    case class :+[A <: Var, B <: Var](a: A, b: B) extends Var
    case class :*[A <: Var, B <: Var](a: A, b: B) extends Var

    object ProveVar extends ProofSystem with RingAxioms[Var, :+, :*, _0.type, _1.type] {}

    object :+ extends ProveVar.Ring_+ {

      override def construct[A <: Var, B <: Var](a: A, b: B): A :+ B = :+(a, b)

      override def deconstruct[A <: Var, B <: Var](v: A :+ B): (A, B) = (v.a, v.b)
    }

    object :* extends ProveVar.Ring_* {

      override val ring_+ : ProveVar.Ring_+ = :+

      override def construct[A <: Var, B <: Var](a: A, b: B): A :* B = :*(a, b)

      override def deconstruct[A <: Var, B <: Var](v: A :* B): (A, B) = (v.a, v.b)
    }

    trait A extends Var
    trait B extends Var
    trait C extends Var

    type AB = A :+ B
    type AB2_1 = AB :* AB

    type AB2_2 = (A :* A) :+ (A :* B) :+ (B :* A) :+ (B :* B)
  }

  object DummyRing extends DummyRing

}

class RingAxiomsSpec extends BaseSpec {

  import org.shapesafe.core.logic.binary.RingAxiomsSpec.DummyRing._
  import ProveVar._

  it("A + B =>> B + A") {

    val tt = forAll[AB].toGoal[B :+ A]

    // manually
    val p1 = tt.useTactic { v =>
      v.cite(:+.commutative)
    }.fulfil

    // automatically
    val p = tt.prove
  }

  it("A + B + C =>> C + B + A") {
    // have to use hypothetical syllogism & substitution

    val tt = forAll[AB :+ C].toGoal[C :+ B :+ A]

    val p1 = tt.useTactic { t =>
      val t1 = t
        .cite(:+.commutative)
        .cite(
          :+.substituteR(
            :+.commutative
          )
        )
        .cite(
          :+.associativeR
        )

      t1
    }.fulfil

    val p = tt.prove
  }

  it("(A + B)^2 =>> AA + 2AB + BB") {}

  it(" ... and vice versa") {}
}
