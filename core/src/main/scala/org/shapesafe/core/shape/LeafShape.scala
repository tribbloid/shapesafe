package org.shapesafe.core.shape

// TODO: add VerifiedShape
trait LeafShape extends Shape {}

object LeafShape {

  import org.shapesafe.core.shape.ProveShape._

  implicit def endo[T <: LeafShape]: T |- T = ProveShape.forAll[T].=>>(identity[T])
}
