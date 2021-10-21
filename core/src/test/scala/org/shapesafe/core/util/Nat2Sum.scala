package org.shapesafe.core.util

import org.shapesafe.core.fixtures.Nat
import org.shapesafe.core.fixtures.Nat._0

trait Nat2Sum extends Nat2ID {

  import ProveNat._

  case class Sum[SRC <: Nat](v: Int) extends Stuff

  implicit def sumAxiom: _0 |- Sum[_0] = forAll[_0].=>> { _ =>
    Sum(0)
  }
}

object Nat2Sum extends Nat2Sum
