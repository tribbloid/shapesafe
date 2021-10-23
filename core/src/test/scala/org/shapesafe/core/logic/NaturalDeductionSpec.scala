package org.shapesafe.core.logic

import org.shapesafe.BaseSpec
import org.shapesafe.core.fixtures.Peano

class NaturalDeductionSpec extends BaseSpec {

  import NaturalDeductionSpec._

  it("should not cause diverging implicit") {

    import ProveStuff._

    implicit def n1[T <: Peano] = forAll[T].=>> { n =>
      N1[T](n.value)
    }

    implicit def n2[T <: Peano] = forAll[N1[T]].=>> { n1 =>
      N2[T](n1.v)
    }

    val proof = forAll[Peano._5].toGoal[N2[_]].prove

    val out = proof(new Peano._5)

    assert(out.v == 5)
  }
}

object NaturalDeductionSpec {

  import org.shapesafe.core.util.Peano2ID._

  case class N1[T <: Peano](v: Int) extends Stuff
  case class N2[T <: Peano](v: Int) extends Stuff

  object ProveStuff extends ProofSystem with NaturalDeduction {}
}
