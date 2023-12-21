package shapesafe.core.logic.binary

import shapesafe.BaseSpec
import shapesafe.core.logic.ProofSystem

object RingAxiomsSpec {

  trait DummyRing {

    trait Var
    object _0 extends Var
    object _1 extends Var

    case class :+[A <: Var, B <: Var](a: A, b: B) extends Var
    case class :*[A <: Var, B <: Var](a: A, b: B) extends Var

    object ProveVar extends ProofSystem with RingAxioms[Var, :+, :*, _0.type, _1.type] {}

    object :+ extends ProveVar.Group_+ {

      override def construct[A <: Var, B <: Var](a: A, b: B): A :+ B = :+(a, b)

      override def deconstruct[A <: Var, B <: Var](v: A :+ B): (A, B) = (v.a, v.b)
    }

    object :* extends ProveVar.Ring_* {

      override val group_+ : ProveVar.Group_+ = :+

      override def construct[A <: Var, B <: Var](a: A, b: B): A :* B = :*(a, b)

      override def deconstruct[A <: Var, B <: Var](v: A :* B): (A, B) = (v.a, v.b)
    }

    trait X extends Var
    trait Y extends Var
    trait Z extends Var

    type XY = X :+ Y
    type XY2 = XY :* XY

    type XY2_1 = (X :* X) :+ (X :* Y) :+ (Y :* X) :+ (Y :* Y)
  }

  object DummyRing extends DummyRing

}

class RingAxiomsSpec extends BaseSpec {

  import shapesafe.core.logic.binary.RingAxiomsSpec.DummyRing._
  import ProveVar._

  it("A + B =>> B + A") {

    val tt = forAll[XY].toGoal[Y :+ X]

    // manually
    tt.useTactic { v =>
      v.cite(:+.commutative)
    }.fulfil

    // automatically
    tt.prove
  }

  it("A + B + C =>> C + B + A") {
    // have to use hypothetical syllogism & substitution

    val tt = forAll[XY :+ Z].toGoal[Z :+ Y :+ X]

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

//    print_@(p1.toString)

//    val pp = tt.prove // TODO: not working, implicit search is too week
//    val pp = tt.proveDirectly
//
//    print_@(pp.toString)
  }

  it("(A + B)^2 =>> AA + 2AB + BB") {}

  it(" ... and vice versa") {}
}
