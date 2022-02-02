package shapesafe.core.shape

import shapeless.Nat

// TODO: should be "Wildcard"?
trait Unchecked extends LeafShape {

  override type Expr = "_UNCHECKED_"

  final override type NatNumOfDimensions = Nat
}

object Unchecked extends Unchecked {}
