package org.shapesafe.core.util

import org.shapesafe.core.fixtures.Nat
import org.shapesafe.core.logic.{ContradictionDeduction, ProofSystem}

trait Nat2ID {

  object ProveStuff extends ProofSystem with ContradictionDeduction
  import ProveStuff._

  abstract class Stuff {
    def v: Int
  }

  object Stuff {

    import Nat._

    implicit def idAxiom: _0 |- ID[_0] = forAll[_0].=>> { _ =>
      ID(0)
    }

    implicit def idTheorem[N <: Nat](
        implicit
        lemma: N |- ID[N]
    ): Inc[N] |- ID[Inc[N]] = forAll[Inc[N]].=>> { nPlus =>
      val p = nPlus.prev
      ID(lemma.apply(p).v + 1)
    }

    // TODO: add back later
    //    implicit def sigmaTheorem[N <: Nat](
    //        implicit
    //        prev1: Inc[N] |- Sigma[N],
    //        prev2: Inc[N] |- ID[Inc[N]]
    //    ): ID[Inc[N]] |- Sigma[Inc[N]] = forAll[ID[Inc[N]]].=>> { idPlus =>
    //      val _sigma = prev1.apply(idPlus.v)
    //      val _id = prev2.apply(idPlus)
    //      Sigma(_sigma.v + _id.v)
    //    }
  }

  case class ID[SRC <: Nat](v: Int) extends Stuff

}

object Nat2ID extends Nat2ID
