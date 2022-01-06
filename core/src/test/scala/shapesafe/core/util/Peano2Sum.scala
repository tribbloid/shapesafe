package shapesafe.core.util

import shapesafe.core.fixtures.Peano
import shapesafe.core.fixtures.Peano._0

trait Peano2Sum extends Peano2ID {

  import ProveStuff._

  case class Sum[SRC <: Peano](v: Int) extends Stuff

  implicit def sumAxiom: _0 |- Sum[_0] = forAll[_0].=>> { _ =>
    Sum(0)
  }
}

object Peano2Sum extends Peano2Sum
