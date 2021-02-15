package org.shapesafe.core.util

import shapeless.{Nat, Witness}

trait Finder {}

object Finder {

  case class ByName[N](w: Witness.Aux[N]) extends Finder {}

  case class ByOrdinal[N <: Nat](n: N) extends Finder {}
}
