package org.shapesafe.core.shape

trait LeafShape extends Shape {}

object LeafShape {

  import org.shapesafe.core.shape.ProveShape.ForAll._

  implicit def endo[T <: LeafShape]: T =>> T = ProveShape.forAll[T].=>>(identity[T])
}
