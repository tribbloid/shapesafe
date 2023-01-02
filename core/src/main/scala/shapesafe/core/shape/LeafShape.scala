package shapesafe.core.shape

import shapeless.Nat
import shapesafe.core.debugging.ExpressionType

// TODO: add VerifiedShape
trait LeafShape extends ShapeType with ExpressionType.Leaf {

  type NatNumOfDimensions <: Nat
}

object LeafShape {

  import shapesafe.core.shape.ProveShape._

  implicit def endo[T <: LeafShape]: T |- T = ProveShape.forAll[T].=>>(identity[T])
}
