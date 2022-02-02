package shapesafe.core.shape

import shapeless.Nat

// TODO: add VerifiedShape
trait LeafShape extends ShapeType {

  type NatNumOfDimensions <: Nat
}

object LeafShape {

  import shapesafe.core.shape.ProveShape._

  implicit def endo[T <: LeafShape]: T |- T = ProveShape.forAll[T].=>>(identity[T])

//  type IfTrue1[_ <: LeafShape] = Const.True
//  type IfTrue2[_ <: LeafShape, _ <: LeafShape] = Const.True
//
//  type IffSameNumOfDimension[L1 <: LeafShape, L2 <: LeafShape] = L1#NatNumOfDimensions =:= L2#NatNumOfDimensions
}
