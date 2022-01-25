package shapesafe.core.shape

// TODO: add VerifiedShape
trait LeafShape extends ShapeType {}

object LeafShape {

  import shapesafe.core.shape.ProveShape._

  implicit def endo[T <: LeafShape]: T |- T = ProveShape.forAll[T].=>>(identity[T])
}
