package org.shapesafe.core.arity

/**
  * always successful, no need to verify
  */
trait VerifiedArity extends Arity.Verifiable {

//  final def in: this.type = this
}

object VerifiedArity {

  import ProveArity.ForAll._

  implicit def endo[T <: VerifiedArity]: T =>> T =
    ProveArity.forAll[T].=>>(identity[T])
}
