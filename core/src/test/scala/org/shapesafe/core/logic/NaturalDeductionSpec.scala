package org.shapesafe.core.logic

import org.shapesafe.BaseSpec
import org.shapesafe.core.fixtures.Nat

class NaturalDeductionSpec extends BaseSpec {

  import NaturalDeductionSpec._

  it("should not cause diverging implicit") {

    import ProveStuff._

    implicit def n1[T <: Nat] = forAll[T].=>> { n =>
      N1[T](n.value)
    }

    implicit def n2[T <: Nat] = forAll[N1[T]].=>> { n1 =>
      N2[T](n1.v)
    }

    val proof = forAll[Nat._5].toGoal[N2[_]].prove

    val out = proof(new Nat._5)

    assert(out.v == 5)
  }
}

object NaturalDeductionSpec {

  import org.shapesafe.core.util.Nat2ID._

  case class N1[T <: Nat](v: Int) extends Stuff
  case class N2[T <: Nat](v: Int) extends Stuff

  object ProveStuff extends ProofSystem with NaturalDeduction {}
}
