package shapesafe.core.shape

import shapeless.Nat

// TODO: should be "Wildcard"?
trait Unchecked extends LeafShape {

  override type Notation = "_UNCHECKED_"

  final override type NatNumOfDimensions = Nat
}

case object Unchecked extends Unchecked {}
