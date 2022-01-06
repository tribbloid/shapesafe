package shapesafe.core.util

import shapesafe.core.fixtures.Peano
import shapesafe.core.logic.{CanUseSubTheory, ContradictionDeduction, ProofSystem}

trait Peano2ID {

  object ProveStuff extends ProofSystem with ContradictionDeduction with CanUseSubTheory
  import ProveStuff._

  abstract class Stuff {
    def v: Int
  }

  object Stuff {

    import Peano._

    implicit def idAxiom: _0 |- ID[_0] = forAll[_0].=>> { _ =>
      ID(0)
    }

    implicit def idTheorem[N <: Peano](
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

  case class ID[SRC <: Peano](v: Int) extends Stuff

}

object Peano2ID extends Peano2ID
