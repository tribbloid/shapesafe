package shapesafe.core.shape

// TODO: add VerifiedShape
trait LeafShape extends Shape {}

object LeafShape {

  import shapesafe.core.shape.ProveShape._

  implicit def endo[T <: LeafShape]: T |- T = ProveShape.forAll[T].=>>(identity[T])
}
